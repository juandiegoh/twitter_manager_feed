package com.twitter_manager

import twitter4j.*

class TwitterFilterStreamReader {

    private final Logger log = Logger.getLogger(getClass())
    public static final double[][] WHOLE_WORLD = [[-180, -90], [180, 90]]
    def stream
    def queueAdapter
    def tweetsCounter = 0

    TwitterFilterStreamReader(queueAdapter) {
        this.stream = new TwitterStreamFactory().instance
        this.queueAdapter = queueAdapter
    }

    def startConsumer() {
        def listener = [
                onStatus: { Status status ->
                    log.info "Count: ${tweetsCounter++}"
                    String statusJson = TwitterObjectFactory.getRawJSON(status);
                    queueAdapter.sendMessage(statusJson)
                },
                onException: { ex -> log.error("onException", ex) },
                onDeletionNotice: { statusDeletionNotice -> },
                onTrackLimitationNotice: { numberOfLimitedStatuses ->
                    log.info("onTrackLimitationNotice - numberOfLimitedStatuses: ${numberOfLimitedStatuses}")
                }
        ] as UserStreamAdapter
        stream.addListener(listener)

        FilterQuery filter = new FilterQuery();
        String[] languages = ['en','es']
        filter.language(languages)
        double[][] location = WHOLE_WORLD
        filter.locations(location)
        stream.filter(filter);
    }
}
