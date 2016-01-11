/**
 *
 * OperationRow
 * Ledger wallet
 *
 * Created by Pierre Pollastri on 11/01/16.
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
package co.ledger.wallet.service.wallet.database.model

import java.util.Date

import co.ledger.wallet.service.wallet.database.cursor.OperationCursor
import org.bitcoinj.core.Coin

class OperationRow(cursor: OperationCursor) {
  val uid = cursor.uid
  val accountIndex = cursor.accountIndex
  val transactionHash = cursor.transactionHash
  val operationType = cursor.operationType
  val value = Coin.valueOf(cursor.value)
  val senders = cursor.senders.split(",")
  val recipients = cursor.recipients.split(",")
  val accountName = cursor.accountName
  val accountColor = cursor.accountColor
  val fees = Coin.valueOf(cursor.fees)
  val time = new Date(cursor.time)
  val lockTime = cursor.lockTime
  val blockHash = cursor.blockHash
  val blockHeight = cursor.blockHeight
}
