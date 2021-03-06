/**
 * Copyright (c) 2013-2020 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.api;

import org.redisson.api.listener.MessageListener;
import reactor.core.publisher.Mono;

/**
 *
 * Reactive interface for Reliable topic based on Redis Stream object.
 * <p>
 * Dedicated Redis connection is allocated per instance (subscriber) of this object.
 * Messages are delivered to all listeners attached to the same Redis setup.
 * <p>
 * Requires <b>Redis 5.0.0 and higher.</b>
 * <p>
 * @author Nikita Koksharov
 *
 */
public interface RReliableTopicReactive extends RExpirableReactive {

    /**
     * Amount of messages stored in Redis Stream object.
     *
     * @return amount of messages
     */
    Mono<Long> sizeAsync();

    /**
     * Publish the message to all subscribers of this topic asynchronously.
     * Each subscriber may have multiple listeners.
     *
     * @param message to send
     * @return number of subscribers that received the message
     */
    Mono<Long> publishAsync(Object message);
    
    /**
     * Subscribes to this topic.
     * <code>MessageListener.onMessage</code> method is called when any message
     * is published on this topic.
     * <p>
     * Watchdog is started when listener was registered.
     *
     * @see org.redisson.config.Config#setReliableTopicWatchdogTimeout(long)
     *
     * @param <M> - type of message
     * @param type - type of message
     * @param listener for messages
     * @return locally unique listener id
     * @see MessageListener
     */
    <M> Mono<String> addListenerAsync(Class<M> type, MessageListener<M> listener);
    
    /**
     * Removes the listener by <code>id</code> for listening this topic
     *
     * @param listenerIds - listener ids
     * @return void
     */
    Mono<Void> removeListenerAsync(String... listenerIds);

    /**
     * Removes all listeners from this topic
     */
    Mono<Void> removeAllListenersAsync();

    /**
     * Returns amount of subscribers to this topic across all Redisson instances.
     * Each subscriber may have multiple listeners.
     *
     * @return amount of subscribers
     */
    Mono<Integer> countSubscribersAsync();

}
