/**
 *
 * events
 * Ledger wallet
 *
 * Created by Pierre Pollastri on 24/11/15.
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
package co.ledger.wallet.wallet

import java.util.Date

import co.ledger.wallet.core.concurrent.AsyncCursor
import org.bitcoinj.core.Coin

package object events {

  object PeerGroupEvents {
    case class StartSynchronization()
    case class StopSynchronization()
    case class SynchronizationProgress(current: Int, total: Int, date: Date)
  }

  object WalletEvents {
    // Clean version

    case class NewBlock(hash: String, height: Int, transactionHashes: Array[String])
    case class NewTransaction(hash: String)
    case class NewOperation(uid: String, accountIndex: Int)
    case class OperationChanged(uid: String, accountIndex: Int)
    case class TransactionConfirmed(hash: String)
    case class TransactionDeleted(hash: String)

    case class MissingAccount(index: Int)
    case class NewAccount(index: Int)

  }

}
