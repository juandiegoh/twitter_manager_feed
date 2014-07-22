package com.twitter_manager

class RabbitProducerService implements QueueAdapter {

    def sendMessage(msg) {
        rabbitSend 'twitter_feed', msg
    }
}
