package com.twitter_manager

class TwitterFeedJob {
    def rabbitProducerService
    def concurrent = false

    static triggers = {
        simple name: 'TwitterFeedJob', startDelay: 100l, repeatInterval: 100l, repeatCount: 0
    }

    def execute() {
        log.info("Starting feed: ")
        try {
            def twitterFilterStreamReader = new TwitterFilterStreamReader(rabbitProducerService)
            twitterFilterStreamReader.startConsumer()
        } catch (Exception exc) {
            log.info("There was an error in the TwitterFeedJob: ", exc)
        }
    }

}