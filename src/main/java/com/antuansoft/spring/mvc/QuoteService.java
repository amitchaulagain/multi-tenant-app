/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.antuansoft.spring.mvc;


import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class QuoteService implements ApplicationListener<BrokerAvailabilityEvent> {

    private static Log logger = LogFactory.getLog(QuoteService.class);

    private final MessageSendingOperations<String> messagingTemplate;


    private AtomicBoolean brokerAvailable = new AtomicBoolean();


    @Autowired
    public QuoteService(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }
//
//    @Scheduled(fixedDelay = 2000)
//    public void sendQuotes() {
//
//        this.messagingTemplate.convertAndSend("/topic/price.stock." + "price", "hrosss");
//    }
//    public void sendNotification() {
//
//        this.messagingTemplate.convertAndSend("/topic/price.stock." + "price", "I am hero");
//    }
//

}
