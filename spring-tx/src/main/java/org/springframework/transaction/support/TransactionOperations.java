/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction.support;

import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionException;

/**
 * Interface specifying basic transaction execution operations.
 * 指定基本事务执行操作的接口。
 * Implemented by {@link TransactionTemplate}. Not often used directly,
 * 由{TransactionTemplate}实现.不经常直接使用，
 * but a useful option to enhance testability, as it can easily be mocked or stubbed.
 * 但这是增强可测试性的一个有用选项，因为它很容易被mock或stub。
 *
 * @author Juergen Hoeller
 * @since 2.0.4
 */
public interface TransactionOperations {

	/**
	 * Execute the action specified by the given callback object within a transaction.
	 * 在事务中执行给定回调对象指定的操作。
	 * <p>Allows for returning a result object created within the transaction, that is,
	 * a domain object or a collection of domain objects.
	 * 允许返回在事务中创建的结果对象,即一个domain对象或者一个domain对象的集合
	 * A RuntimeException thrown by the callback is treated as a fatal exception that enforces a rollback.
	 * 回调引发的RuntimeException被视为强制回滚的致命异常。
	 * Such an exception gets propagated to the caller of the template.
	 * 这样的异常会传播到template的调用方。
	 * @param action the callback object that specifies the transactional action 指定事务操作的回调对象
	 * @return a result object returned by the callback, or {@code null} if none 回调返回的结果对象，如果没有，则返回null
	 * @throws TransactionException in case of initialization, rollback, or system errors 初始化、回滚或系统错误时
	 * @throws RuntimeException if thrown by the TransactionCallback 由回调引发的异常
	 */
	@Nullable
	<T> T execute(TransactionCallback<T> action) throws TransactionException;

}
