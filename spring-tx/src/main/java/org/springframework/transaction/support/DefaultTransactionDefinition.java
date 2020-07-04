/*
 * Copyright 2002-2018 the original author or authors.
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

import java.io.Serializable;

import org.springframework.core.Constants;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionDefinition;

/**
 * Default implementation of the {@link TransactionDefinition} interface,
 * offering bean-style configuration and sensible default values
 * (PROPAGATION_REQUIRED, ISOLATION_DEFAULT, TIMEOUT_DEFAULT, readOnly=false).
 * {@link TransactionDefinition}接口的默认实现，
 * 提供bean风格的配置和合理的默认值（PROPAGATION_REQUIRED、ISOLATION_DEFAULT、TIMEOUT_DEFAULT、readOnly=false）。
 *
 * <p>Base class for both {@link TransactionTemplate} and
 * {@link org.springframework.transaction.interceptor.DefaultTransactionAttribute}.
 * TransactionTemplate和DefaultTransactionAttribute的基类
 * @author Juergen Hoeller
 * @since 08.05.2003
 */
@SuppressWarnings("serial")
public class DefaultTransactionDefinition implements TransactionDefinition, Serializable {

	/** Prefix for the propagation constants defined in TransactionDefinition. */
	// TransactionDefinition中定义的传播常量的前缀
	public static final String PREFIX_PROPAGATION = "PROPAGATION_";

	/** Prefix for the isolation constants defined in TransactionDefinition. */
	// TransactionDefinition中定义的隔离常量的前缀。
	public static final String PREFIX_ISOLATION = "ISOLATION_";

	/** Prefix for transaction timeout values in description strings. */
	// 描述事务超时String的前缀。
	public static final String PREFIX_TIMEOUT = "timeout_";

	/** Marker for read-only transactions in description strings. */
	// 描述只读事务String。
	public static final String READ_ONLY_MARKER = "readOnly";


	/** Constants instance for TransactionDefinition. */
	static final Constants constants = new Constants(TransactionDefinition.class);

	private int propagationBehavior = PROPAGATION_REQUIRED;

	private int isolationLevel = ISOLATION_DEFAULT;

	private int timeout = TIMEOUT_DEFAULT;

	private boolean readOnly = false;

	@Nullable
	private String name;


	/**
	 * Create a new DefaultTransactionDefinition, with default settings.
	 * 使用默认设置创建新的DefaultTransactionDefinition。
	 * Can be modified through bean property setters.
	 * 可以通过bean属性设置器进行修改。
	 * @see #setPropagationBehavior
	 * @see #setIsolationLevel
	 * @see #setTimeout
	 * @see #setReadOnly
	 * @see #setName
	 */
	public DefaultTransactionDefinition() {
	}

	/**
	 * Copy constructor. Definition can be modified through bean property setters.
	 * 复制构造函数。可以通过bean属性设置器修改属性。
	 * @see #setPropagationBehavior
	 * @see #setIsolationLevel
	 * @see #setTimeout
	 * @see #setReadOnly
	 * @see #setName
	 */
	public DefaultTransactionDefinition(TransactionDefinition other) {
		this.propagationBehavior = other.getPropagationBehavior();
		this.isolationLevel = other.getIsolationLevel();
		this.timeout = other.getTimeout();
		this.readOnly = other.isReadOnly();
		this.name = other.getName();
	}

	/**
	 * Create a new DefaultTransactionDefinition with the given
	 * propagation behavior. Can be modified through bean property setters.
	 * 使用给定的传播行为创建新的DefaultTransactionDefinition。可以通过bean属性设置器进行修改。
	 * @param propagationBehavior one of the propagation constants in the
	 * TransactionDefinition interface
	 * @see #setIsolationLevel
	 * @see #setTimeout
	 * @see #setReadOnly
	 */
	public DefaultTransactionDefinition(int propagationBehavior) {
		this.propagationBehavior = propagationBehavior;
	}


	/**
	 * Set the propagation behavior by the name of the corresponding constant in
	 * TransactionDefinition, e.g. "PROPAGATION_REQUIRED".
	 * 通过TransactionDefinition中相应常量的名称设置传播行为，例如“PROPAGATION_REQUIRED”
	 * @param constantName name of the constant
	 * @throws IllegalArgumentException if the supplied value is not resolvable
	 * to one of the {@code PROPAGATION_} constants or is {@code null}
	 * @see #setPropagationBehavior
	 * @see #PROPAGATION_REQUIRED
	 */
	public final void setPropagationBehaviorName(String constantName) throws IllegalArgumentException {
		if (!constantName.startsWith(PREFIX_PROPAGATION)) {
			throw new IllegalArgumentException("Only propagation constants allowed");
		}
		setPropagationBehavior(constants.asNumber(constantName).intValue());
	}

	/**
	 * Set the propagation behavior. Must be one of the propagation constants
	 * in the TransactionDefinition interface. Default is PROPAGATION_REQUIRED.
	 * 设置传播行为。必须是TransactionDefinition接口中的传播常量之一。默认为“需要传播”。
	 * <p>Exclusively designed for use with {@link #PROPAGATION_REQUIRED} or
	 * {@link #PROPAGATION_REQUIRES_NEW} since it only applies to newly started
	 * transactions.
	 * PROPAGATION_REQUIRES_NEW只适用于新启动的事务
	 * Consider switching the "validateExistingTransactions" flag to
	 * "true" on your transaction manager if you'd like isolation level declarations
	 * to get rejected when participating in an existing transaction with a different
	 * isolation level.
	 * 如果您希望在参与具有不同隔离级别的现有事务时拒绝隔离级别声明，
	 * 请考虑将事务管理器上的“validateExistingTransactions”标志切换为“true”。
	 * <p>Note that a transaction manager that does not support custom isolation levels
	 * will throw an exception when given any other level than {@link #ISOLATION_DEFAULT}.
	 * 请注意，不支持自定义隔离级别的事务管理器将在给定{ISOLATION_DEFAULT}之外的任何级别时引发异常。
	 * @throws IllegalArgumentException if the supplied value is not one of the {@code PROPAGATION_} constants
	 * 如果传入的值不是PROPAGATION_常量中的一个，抛出IllegalArgumentException异常
	 * @see #PROPAGATION_REQUIRED
	 */
	public final void setPropagationBehavior(int propagationBehavior) {
		if (!constants.getValues(PREFIX_PROPAGATION).contains(propagationBehavior)) {
			throw new IllegalArgumentException("Only values of propagation constants allowed");
		}
		this.propagationBehavior = propagationBehavior;
	}

	@Override
	public final int getPropagationBehavior() {
		return this.propagationBehavior;
	}

	/**
	 * Set the isolation level by the name of the corresponding constant in
	 * TransactionDefinition, e.g. "ISOLATION_DEFAULT".
	 * 通过TransactionDefinition中相应常量的名称设置隔离级别，例如“ISOLATION_DEFAULT”。
	 * @param constantName name of the constant
	 * @throws IllegalArgumentException if the supplied value is not resolvable
	 * to one of the {@code ISOLATION_} constants or is {@code null}
	 * 如果传入了一个非ISOLATION_常量或者null，抛出IllegalArgumentException异常
	 * @see #setIsolationLevel
	 * @see #ISOLATION_DEFAULT
	 */
	public final void setIsolationLevelName(String constantName) throws IllegalArgumentException {
		if (!constantName.startsWith(PREFIX_ISOLATION)) {
			throw new IllegalArgumentException("Only isolation constants allowed");
		}
		setIsolationLevel(constants.asNumber(constantName).intValue());
	}

	/**
	 * Set the isolation level. Must be one of the isolation constants
	 * in the TransactionDefinition interface. Default is ISOLATION_DEFAULT.
	 * 设置隔离级别。必须是TransactionDefinition接口中的隔离常量之一。默认值为ISOLATION_DEFAULT。
	 * <p>Exclusively designed for use with {@link #PROPAGATION_REQUIRED} or
	 * {@link #PROPAGATION_REQUIRES_NEW} since it only applies to newly started
	 * transactions.
	 * 专门设计用于{@link#PROPAGATION_REQUIRED}或{@link#PROPAGATION_REQUIRED_NEW}，因为它只适用于新启动的事务。
	 * Consider switching the "validateExistingTransactions" flag to
	 * "true" on your transaction manager if you'd like isolation level declarations
	 * to get rejected when participating in an existing transaction with a different
	 * isolation level.
	 * 如果您希望在参与具有不同隔离级别的现有事务时拒绝隔离级别声明，
	 * 请考虑将事务管理器上的“validateExistingTransactions”标志切换为“true”
	 * <p>Note that a transaction manager that does not support custom isolation levels
	 * will throw an exception when given any other level than {@link #ISOLATION_DEFAULT}.
	 * 请注意，不支持自定义隔离级别的事务管理器在给定{@link#ISOLATION_DEFAULT}以外的任何级别时都将引发异常。
	 * @throws IllegalArgumentException if the supplied value is not one of the
	 * {@code ISOLATION_} constants
	 * @see #ISOLATION_DEFAULT
	 */
	public final void setIsolationLevel(int isolationLevel) {
		if (!constants.getValues(PREFIX_ISOLATION).contains(isolationLevel)) {
			throw new IllegalArgumentException("Only values of isolation constants allowed");
		}
		this.isolationLevel = isolationLevel;
	}

	@Override
	public final int getIsolationLevel() {
		return this.isolationLevel;
	}

	/**
	 * Set the timeout to apply, as number of seconds.
	 * Default is TIMEOUT_DEFAULT (-1).
	 * 设置要应用的超时，以秒为单位。默认值为超时默认值（-1）。
	 * <p>Exclusively designed for use with {@link #PROPAGATION_REQUIRED} or
	 * {@link #PROPAGATION_REQUIRES_NEW} since it only applies to newly started
	 * transactions.
	 * 专门设计用于{@link#PROPAGATION_REQUIRED}或{@link#PROPAGATION_REQUIRED_NEW}，因为它只适用于新启动的事务。
	 * <p>Note that a transaction manager that does not support timeouts will throw
	 * an exception when given any other timeout than {@link #TIMEOUT_DEFAULT}.
	 * 请注意，不支持超时的事务管理器将在给定{@link#TIMEOUT_DEFAULT}之外的任何其他超时时抛出异常。
	 * @see #TIMEOUT_DEFAULT
	 */
	public final void setTimeout(int timeout) {
		if (timeout < TIMEOUT_DEFAULT) {
			throw new IllegalArgumentException("Timeout must be a positive integer or TIMEOUT_DEFAULT");
		}
		this.timeout = timeout;
	}

	@Override
	public final int getTimeout() {
		return this.timeout;
	}

	/**
	 * Set whether to optimize as read-only transaction.
	 * Default is "false".
	 * 设置是否优化为只读事务。默认值为“false”。
	 * <p>The read-only flag applies to any transaction context, whether backed
	 * by an actual resource transaction ({@link #PROPAGATION_REQUIRED}/
	 * {@link #PROPAGATION_REQUIRES_NEW}) or operating non-transactionally at
	 * the resource level ({@link #PROPAGATION_SUPPORTS}).
	 * 只读标志适用于任何事务上下文，无论是由实际的资源事务（{@link#PROPAGATION_REQUIRED}/{@link#PROPAGATION_REQUIRED_NEW}）支持，
	 * 还是在资源级别以非事务方式操作（{@link#PROPAGATION_SUPPORTS}）支持
	 * In the latter case,
	 * the flag will only apply to managed resources within the application,
	 * such as a Hibernate {@code Session}.
	 * 在后一种情况下，该标志将只应用于应用程序中的托管资源，例如Hibernate{@code Session}。
	 * <p>This just serves as a hint for the actual transaction subsystem;
	 * it will <i>not necessarily</i> cause failure of write access attempts.
	 * 这只是对实际事务子系统的提示；它不一定会导致写访问尝试失败。
	 * A transaction manager which cannot interpret the read-only hint will
	 * <i>not</i> throw an exception when asked for a read-only transaction.
	 * 如果一个事务管理器不能理解只读提示，当被请求一个只读事务时，将会抛出异常
	 */
	public final void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public final boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * Set the name of this transaction. Default is none.
	 * 设置此事务的名称。默认值为“无”。
	 * <p>This will be used as transaction name to be shown in a
	 * transaction monitor, if applicable (for example, WebLogic's).
	 * 这将被用作要在事务监视器中显示的事务名称（如WebLogic）。
	 */
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	@Nullable
	public final String getName() {
		return this.name;
	}


	/**
	 * This implementation compares the {@code toString()} results.
	 * 此实现比较{@code toString（）}结果。
	 * @see #toString()
	 */
	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof TransactionDefinition && toString().equals(other.toString())));
	}

	/**
	 * This implementation returns {@code toString()}'s hash code.
	 * 此实现返回{@code toString（）}的哈希代码。
	 * @see #toString()
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Return an identifying description for this transaction definition.
	 * 返回此事务定义的标识说明。
	 * <p>The format matches the one used by
	 * {@link org.springframework.transaction.interceptor.TransactionAttributeEditor},
	 * to be able to feed {@code toString} results into bean properties of type
	 * {@link org.springframework.transaction.interceptor.TransactionAttribute}.
	 * 该格式与{TransactionAttributeEditor}使用的格式相匹配，toString()方法将生成{TransactionAttribute}类型的bean属性。
	 * <p>Has to be overridden in subclasses for correct {@code equals}
	 * and {@code hashCode} behavior. Alternatively, {@link #equals}
	 * and {@link #hashCode} can be overridden themselves.
	 * 必须在子类中重写以获得正确的{@code equals}和{@code hashCode}行为。
	 * 或者，{@link#equals}和{@link#hashCode}可以自己重写。
	 * @see #getDefinitionDescription()
	 * @see org.springframework.transaction.interceptor.TransactionAttributeEditor
	 */
	@Override
	public String toString() {
		return getDefinitionDescription().toString();
	}

	/**
	 * Return an identifying description for this transaction definition.
	 * 返回此事务定义的标识说明。
	 * <p>Available to subclasses, for inclusion in their {@code toString()} result.
	 * 可用于子类，将其包含在toString()方法返回结果中
	 */
	protected final StringBuilder getDefinitionDescription() {
		StringBuilder result = new StringBuilder();
		result.append(constants.toCode(this.propagationBehavior, PREFIX_PROPAGATION));
		result.append(',');
		result.append(constants.toCode(this.isolationLevel, PREFIX_ISOLATION));
		if (this.timeout != TIMEOUT_DEFAULT) {
			result.append(',');
			result.append(PREFIX_TIMEOUT).append(this.timeout);
		}
		if (this.readOnly) {
			result.append(',');
			result.append(READ_ONLY_MARKER);
		}
		return result;
	}

}
