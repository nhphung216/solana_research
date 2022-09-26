package com.solana.models.buffer

import com.solana.core.PublicKeyRule
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.BorshInput
import com.solana.vendor.borshj.BorshOutput
import com.solana.vendor.borshj.BorshRule
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountInfo(
    val name: Short,
    val totalDepositedAmount: Long,
//    val minimumDepositedAmount: Double,
//    val penaltyRate: Double,
//    val stakingDuration: Double,
//    val rewardPeriod: Double,
//    val startAt: Double,
) : BorshCodable

class AccountInfoRule(
    override val clazz: Class<AccountInfo> = AccountInfo::class.java
) : BorshRule<AccountInfo> {
    override fun read(input: BorshInput): AccountInfo {
        val name = input.readU16()
        val totalDepositedAmount: Long = input.readU64()
//        val minimumDepositedAmount: Double = input.readF64()
//        val penaltyRate: Double = input.readF64()
//        val stakingDuration: Double = input.readF64()
//        val rewardPeriod: Double = input.readF64()
//        val startAt: Double = input.readF64()
        return AccountInfo(
            name,
            totalDepositedAmount,
//            minimumDepositedAmount,
//            penaltyRate,
//            stakingDuration,
//            rewardPeriod,
//            startAt
        )
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        val accountInfo = obj as AccountInfo
        output.writeU16(accountInfo.name)
        output.writeU64(accountInfo.totalDepositedAmount)
//        output.writeF64(accountInfo.minimumDepositedAmount)
//        output.writeF64(accountInfo.penaltyRate)
//        output.writeF64(accountInfo.stakingDuration)
//        output.writeF64(accountInfo.rewardPeriod)
//        output.writeF64(accountInfo.startAt)
        return PublicKeyRule().writeZeros(output)
    }
}
