/**
 *
 * Account
 * Ledger wallet
 *
 * Created by Pierre Pollastri on 23/11/15.
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

import co.ledger.wallet.core.concurrent.AsyncCursor
import org.bitcoinj.core.{Transaction, Address, Coin}
import org.bitcoinj.crypto.DeterministicKey

import scala.concurrent.Future

trait Account {

  def index: Int
  def wallet: Wallet

  def freshPublicAddress(): Future[Address]
  def xpub(): Future[DeterministicKey]
  def balance(): Future[Coin]
  def synchronize(provider: ExtendedPublicKeyProvider): Future[Unit]
  def operations(batchSize: Int = Wallet.DefaultOperationsBatchSize): Future[AsyncCursor[Operation]]
  def utxo(targetValue: Option[Coin] = None): Future[Array[Utxo]]

}
