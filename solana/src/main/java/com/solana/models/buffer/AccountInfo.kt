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
    val minimumDepositedAmount: Long,
    val penaltyRate: Long,
    val stakingDuration: Long,
    val rewardPeriod: Long,
    val startAt: Long,
) : BorshCodable

class AccountInfoRule(
    override val clazz: Class<AccountInfo> = AccountInfo::class.java
) : BorshRule<AccountInfo> {
    override fun read(input: BorshInput): AccountInfo {
        val name = input.readU16()
        val totalDepositedAmount: Long = input.readU64()
        val minimumDepositedAmount: Long = input.readU64()
        val penaltyRate: Long = input.readU64()
        val stakingDuration: Long = input.readU64()
        val rewardPeriod: Long = input.readU64()
        val startAt: Long = input.readU64()
        return AccountInfo(
            name,
            totalDepositedAmount,
            minimumDepositedAmount,
            penaltyRate,
            stakingDuration,
            rewardPeriod,
            startAt
        )
    }

    override fun <Self> write(obj: Any, output: BorshOutput<Self>): Self {
        val accountInfo = obj as AccountInfo
        output.writeU16(accountInfo.name)
        output.writeU64(accountInfo.totalDepositedAmount)
        output.writeU64(accountInfo.minimumDepositedAmount)
        output.writeU64(accountInfo.penaltyRate)
        output.writeU64(accountInfo.stakingDuration)
        output.writeU64(accountInfo.rewardPeriod)
        output.writeU64(accountInfo.startAt)
        return PublicKeyRule().writeZeros(output)
    }
}
