/**
 *
 * LedgerDerivationApi
 * Ledger wallet
 *
 * Created by Pierre Pollastri on 26/01/16.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Ledger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package co.ledger.wallet.core.device.api

import co.ledger.wallet.core.bitcoin.Base58
import co.ledger.wallet.core.crypto.{Crypto, Hash160}
import co.ledger.wallet.core.utils.{AsciiUtils, BytesReader, BytesWriter}
import co.ledger.wallet.wallet.DerivationPath
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.crypto.DeterministicKey

import scala.concurrent.Future

trait LedgerDerivationApi extends LedgerFirmwareApi {
  import LedgerCommonApiInterface._
  import LedgerDerivationApi._

  def derivePublicAddress(path: DerivationPath, networkParameters: NetworkParameters)
    : Future[PublicAddressResult] =
    firmwareVersion() flatMap {(version) =>
      if (version.usesDeprecatedBip32Derivation) {
        throw LedgerUnsupportedFirmwareException()
      }
      val writer = new BytesWriter(path.length * 4 + 1)
      writer.writeByte(path.length)
      for (i <- 0 to path.depth) {
        val n = path(i).get
        writer.writeInt(n.childNum)
      }
      $$(s"GET PUBLIC ADDRESS $path") {
        sendApdu(0xe0, 0x40, 0x00, 0x00, writer.toByteArray, 0x00) map {(result) =>
          matchErrorsAndThrow(result)
          PublicAddressResult(result.data)
        }
      }
    }

  def deriveExtendedPublicKey(path: DerivationPath, network: NetworkParameters): Future[DeterministicKey] = {

    def finalize(fingerprint: Long): Future[DeterministicKey] = {
      derivePublicAddress(path, network) map {(result) =>
        val magic = network.getBip32HeaderPub
        val depth = path.length.toByte
        val childNum = path.childNum
        val chainCode = result.chainCode
        val publicKey = Crypto.compressPublicKey(result.publicKey)
        val rawXpub = new BytesWriter(13 + chainCode.length + publicKey.length)
        rawXpub.writeInt(magic)
        rawXpub.writeByte(depth)
        rawXpub.writeInt(fingerprint)
        rawXpub.writeInt(childNum)
        rawXpub.writeByteArray(chainCode)
        rawXpub.writeByteArray(publicKey)
        val xpub58 = Base58.encodeWitchChecksum(rawXpub.toByteArray)
        DeterministicKey.deserializeB58(xpub58, network)
      }
    }

    if (path.depth > 0) {
      derivePublicAddress(path.parent, network) flatMap {(result) =>
        val hash160 = Hash160.hash(Crypto.compressPublicKey(result.publicKey))
        val fingerprint: Long =
            ((hash160(0) & 0xFFL) << 24) |
            ((hash160(1) & 0xFFL) << 16) |
            ((hash160(2) & 0xFFL) << 8) |
            (hash160(3) & 0xFFL)
        finalize(fingerprint)
      }
    } else {
      finalize(0)
    }
  }

}
object LedgerDerivationApi {

  case class PublicAddressResult(publicKey: Array[Byte],
                                 address: String,
                                 chainCode: Array[Byte]
                                  ) {

  }

  object PublicAddressResult {

    def apply(reader: BytesReader): PublicAddressResult = {
      var length = reader.readNextByte() & 0xFF
      val publicKey = reader.readNextBytes(length)
      length = reader.readNextByte() & 0xFF
      val address = AsciiUtils.toString(reader.readNextBytes(length))
      val chainCode = reader.readNextBytesUntilEnd()
      new PublicAddressResult(publicKey, address, chainCode)
    }

  }

}