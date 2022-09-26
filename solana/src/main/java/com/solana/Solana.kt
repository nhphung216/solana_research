package com.solana

import com.solana.actions.Action
import com.solana.api.Api
import com.solana.core.HotAccount
import com.solana.models.Token
import com.solana.networking.NetworkingRouter
import com.solana.networking.socket.SolanaSocket
import com.solana.vendor.TokensListParser

 class Solana(val router: NetworkingRouter){
    val api: Api = Api(router)
    val socket: SolanaSocket = SolanaSocket(router.endpoint)
    val supportedTokens: List<Token> by lazy {
        TokensListParser().parse(router.endpoint.network.name).getOrThrows()
    }
    val action: Action = Action(api, supportedTokens)
}
