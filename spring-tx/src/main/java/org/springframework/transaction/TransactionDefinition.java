/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.transaction;

import org.springframework.lang.Nullable;

/**
 * Interface that defines Spring-compliant transaction properties.
 * 定义符合Spring的事务属性的接口。
 * Based on the propagation behavior definitions analogous to EJB CMT attributes.
 * 基于类似于EJB CMT属性的传播行为定义。
 *
 * <p>Note that isolation level and timeout settings will not get applied unless
 * an actual new transaction gets started.
 * 请注意，除非启动实际的新事务，否则将不会应用隔离级别和超时设置。
 * As only {@link #PROPAGATION_REQUIRED},
 * {@link #PROPAGATION_REQUIRES_NEW} and {@link #PROPAGATION_NESTED} can cause
 * that, it usually doesn't make sense to specify those settings in other cases.
 * 由于只有{@link#PROPAGATION#必需}，{@link#PROPAGATION#需要{NEW}
 * 和{@link#PROPAGATION#嵌套}可以导致这种情况，所以在其他情况下指定这些设置通常没有意义。
 * Furthermore, be aware that not all transaction managers will support those
 * advanced features and thus might throw corresponding exceptions when given
 * non-default values.
 * 此外，请注意，并非所有事务管理器都支持这些高级功能，因此在给定非默认值时可能抛出相应的异常。
 *
 * <p>The {@link #isReadOnly() read-only flag} applies to any transaction context,
 * whether backed by an actual resource transaction or operating non-transactionally
 * at the resource level.
 * {isReadOnly只读标志}适用于任何事务上下文，无论是由实际的资源事务支持还是在资源级别非事务性操作。
 * In the latter case, the flag will only apply to managed
 * resources within the application, such as a Hibernate {@code Session}.
 * 在后一种情况下，标志将只应用于应用程序中的托管资源，例如Hibernate{@code Session}。
 *
 * @author Juergen Hoeller
 * @since 08.05.2003
 * @see PlatformTransactionManager#getTransaction(TransactionDefinition)
 * @see org.springframework.transaction.support.DefaultTransactionDefinition
 * @see org.springframework.transaction.interceptor.TransactionAttribute
 */
public interface TransactionDefinition {

	/**
	 * Support a current transaction; create a new one if none exists.
	 * 支持当前事务；如果不存在，则创建新事务。
	 * Analogous to the EJB transaction attribute of the same name.
	 * 类似于同名的EJB事务属性。
	 * <p>This is typically the default setting of a transaction definition,
	 * and typically defines a transaction synchronization scope.
	 * 这通常是事务定义的默认设置，并且通常定义事务同步范围。
	 */
	int PROPAGATION_REQUIRED = 0;

	/**
	 * Support a current transaction; execute non-transactionally if none exists.
	 * 支持当前事务；如果不存在，则以非事务方式执行。
	 * Analogous to the EJB transaction attribute of the same name.
	 * 类似于同名的EJB事务属性。
	 * <p><b>NOTE:</b> For transaction managers with transaction synchronization,
	 * {@code PROPAGATION_SUPPORTS} is slightly different from no transaction
	 * at all, as it defines a transaction scope that synchronization might apply to.
	 * 对于具有事务同步的事务管理器，{@code PROPAGATION_SUPPORTS}与根本没有事务稍有不同，因为它定义了一个可以应用于同步的事务作用域。
	 * As a consequence, the same resources (a JDBC {@code Connection}, a
	 * Hibernate {@code Session}, etc) will be shared for the entire specified
	 * scope. Note that the exact behavior depends on the actual synchronization
	 * configuration of the transaction manager!
	 * 因此，相同的资源（JDBC{@code Connection}、Hibernate{@code Session}）将在整个指定范围内共享。
	 * 请注意，具体行为取决于事务管理器的实际同步配置！
	 * <p>In general, use {@code PROPAGATION_SUPPORTS} with care! In particular, do
	 * not rely on {@code PROPAGATION_REQUIRED} or {@code PROPAGATION_REQUIRES_NEW}
	 * <i>within</i> a {@code PROPAGATION_SUPPORTS} scope (which may lead to
	 * synchronization conflicts at runtime).
	 * 一般来说用PROPAGATION_REQUIRED要小心！特别是不要依赖PROPAGATION_REQUIRED
	 * 或PROPAGATION_REQUIRES_NEW内部的PROPAGATION_SUPPORTS（这可能会导致运行时的同步冲突）
	 * If such nesting is unavoidable, make sure
	 * to configure your transaction manager appropriately (typically switching to
	 * "synchronization on actual transaction").
	 * 如果这种嵌套是不可避免的，请确保适当地配置事务管理器（通常切换到“实际事务上的同步”）。
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setTransactionSynchronization
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
	 */
	int PROPAGATION_SUPPORTS = 1;

	/**
	 * Support a current transaction; throw an exception if no current transaction
	 * exists. Analogous to the EJB transaction attribute of the same name.
	 * 支持当前事务；如果不存在当前事务，则抛出异常。类似于同名的EJB事务属性。
	 * <p>Note that transaction synchronization within a {@code PROPAGATION_MANDATORY}
	 * scope will always be driven by the surrounding transaction.
	 * PROPAGATION_MANDATORY事务的同步始终由环绕的事务驱动
	 */
	int PROPAGATION_MANDATORY = 2;

	/**
	 * Create a new transaction, suspending the current transaction if one exists.
	 * 创建新事务，如果当前事务存在，则挂起当前事务。
	 * Analogous to the EJB transaction attribute of the same name.
	 * 类似于同名的EJB事务属性。
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers.
	 * 实际的事务暂停不会在所有事务管理器上开箱即用。
	 * This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the {@code javax.transaction.TransactionManager} to be
	 * made available it to it (which is server-specific in standard Java EE).
	 * 这尤其适用于{JtaTransactionManager}，它要求{TransactionManager}对它可用（这在标准javaee中是特定于服务器的）。
	 * <p>A {@code PROPAGATION_REQUIRES_NEW} scope always defines its own
	 * transaction synchronizations. Existing synchronizations will be suspended
	 * and resumed appropriately.
	 * PROPAGATION_REQUIRES_NEW作用域总是定义自己的事务同步。现有同步将被挂起并适当地恢复。
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	int PROPAGATION_REQUIRES_NEW = 3;

	/**
	 * Do not support a current transaction; rather always execute non-transactionally.
	 * Analogous to the EJB transaction attribute of the same name.
	 * 不支持当前事务；而是始终以非事务方式执行。类似于同名的EJB事务属性。
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the {@code javax.transaction.TransactionManager} to be
	 * made available it to it (which is server-specific in standard Java EE).
	 * 实际的事务暂停不会在所有事务管理器上开箱即用。这尤其适用于{JtaTransactionManager}，
	 * 它需要{TransactionManager}提供给它（这是标准javaee中特定于服务器的）。
	 * <p>Note that transaction synchronization is <i>not</i> available within a
	 * {@code PROPAGATION_NOT_SUPPORTED} scope. Existing synchronizations
	 * will be suspended and resumed appropriately.
	 * 注意，事务同步在{PROPAGATION_not_SUPPORTED}范围内是不可用的。现有同步将被挂起并适当地恢复。
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	int PROPAGATION_NOT_SUPPORTED = 4;

	/**
	 * Do not support a current transaction; throw an exception if a current transaction
	 * exists. Analogous to the EJB transaction attribute of the same name.
	 * 不支持当前事务；如果当前事务存在，则引发异常。类似于同名的EJB事务属性。
	 * <p>Note that transaction synchronization is <i>not</i> available within a
	 * {@code PROPAGATION_NEVER} scope.
	 * 注意，事务同步在{PROPAGATION_NEVER}范围内是不可用的。
	 */
	int PROPAGATION_NEVER = 5;

	/**
	 * Execute within a nested transaction if a current transaction exists,
	 * behave like {@link #PROPAGATION_REQUIRED} otherwise. There is no
	 * analogous feature in EJB.
	 * 如果当前事务存在，则在嵌套事务中执行，否则的行为类似于{PROPAGATION_REQUIRED}。EJB中没有类似的特性。
	 * <p><b>NOTE:</b> Actual creation of a nested transaction will only work on
	 * specific transaction managers.
	 * 实际创建嵌套事务只会在特定的事务管理器上工作。
	 * Out of the box, this only applies to the JDBC
	 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager}
	 * when working on a JDBC 3.0 driver. Some JTA providers might support
	 * nested transactions as well.
	 * 开箱即用，这只适用于jdbc3.0驱动程序时的JDBC{DataSourceTransactionManager}。一些JTA提供者也可能支持嵌套事务。
	 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
	 */
	int PROPAGATION_NESTED = 6;


	/**
	 * Use the default isolation level of the underlying datastore.
	 * 使用底层数据存储的默认隔离级别。
	 * All other levels correspond to the JDBC isolation levels.
	 * 所有其他级别都对应于JDBC隔离级别。
	 * @see java.sql.Connection
	 */
	int ISOLATION_DEFAULT = -1;

	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * can occur.
	 * 指示可能发生脏读、不可重复读和幻象读。
	 * <p>This level allows a row changed by one transaction to be read by another
	 * transaction before any changes in that row have been committed (a "dirty read").
	 * If any of the changes are rolled back, the second transaction will have
	 * retrieved an invalid row.
	 * 此级别允许由一个事务更改的行在提交该行中的任何更改之前被另一个事务读取（“脏读”）。如果回滚任何更改，则第二个事务将检索到无效行。
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 */
	int ISOLATION_READ_UNCOMMITTED = 1;  // same as java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

	/**
	 * Indicates that dirty reads are prevented; non-repeatable reads and
	 * phantom reads can occur.
	 * 指示阻止脏读；可能发生不可重复读和幻象读。
	 * <p>This level only prohibits a transaction from reading a row
	 * with uncommitted changes in it.
	 * 此级别仅禁止事务读取包含未提交更改的行。
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 */
	int ISOLATION_READ_COMMITTED = 2;  // same as java.sql.Connection.TRANSACTION_READ_COMMITTED;

	/**
	 * Indicates that dirty reads and non-repeatable reads are prevented;
	 * phantom reads can occur.
	 * 指示阻止脏读和不可重复读；可能发生虚读。
	 * <p>This level prohibits a transaction from reading a row with uncommitted changes
	 * in it, and it also prohibits the situation where one transaction reads a row,
	 * a second transaction alters the row, and the first transaction re-reads the row,
	 * getting different values the second time (a "non-repeatable read").
	 * 此级别禁止事务读取包含未提交更改的行，还禁止一个事务读取行，第二个事务更改行，
	 * 第一个事务重新读取该行，第二次获取不同的值（“不可重复读取”）。
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 */
	int ISOLATION_REPEATABLE_READ = 4;  // same as java.sql.Connection.TRANSACTION_REPEATABLE_READ;

	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * are prevented.
	 * 指示阻止脏读、不可重复读和虚读。
	 * <p>This level includes the prohibitions in {@link #ISOLATION_REPEATABLE_READ}
	 * and further prohibits the situation where one transaction reads all rows that
	 * satisfy a {@code WHERE} condition, a second transaction inserts a row
	 * that satisfies that {@code WHERE} condition, and the first transaction
	 * re-reads for the same condition, retrieving the additional "phantom" row
	 * in the second read.
	 * 此级别包括{ISOLATION_REPEATABLE_READ}中的禁止，并进一步禁止这样的情况：
	 * 一个事务读取满足{@code where}条件的所有行，第二个事务插入满足{@code where}条件的行，
	 * 而第一个事务针对相同的条件重新读取，在第二次读取中检索附加的“幻象”行。
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 */
	int ISOLATION_SERIALIZABLE = 8;  // same as java.sql.Connection.TRANSACTION_SERIALIZABLE;


	/**
	 * Use the default timeout of the underlying transaction system,
	 * or none if timeouts are not supported.
	 * 使用基础事务系统的默认超时，如果不支持超时，则使用无。
	 */
	int TIMEOUT_DEFAULT = -1;


	/**
	 * Return the propagation behavior.
	 * 返回传播行为。
	 * <p>Must return one of the {@code PROPAGATION_XXX} constants
	 * defined on {@link TransactionDefinition this interface}.
	 * 必须返回在{@link TransactionDefinition this interface}上定义的{@code PROPAGATION_XXX}常量之一
	 * <p>The default is {@link #PROPAGATION_REQUIRED}.
	 * 默认值是{@link#PROPAGATION_REQUIRED}
	 * @return the propagation behavior
	 * @see #PROPAGATION_REQUIRED
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isActualTransactionActive()
	 */
	default int getPropagationBehavior() {
		return PROPAGATION_REQUIRED;
	}

	/**
	 * Return the isolation level.
	 * 返回隔离级别。
	 * <p>Must return one of the {@code ISOLATION_XXX} constants defined on
	 * {@link TransactionDefinition this interface}. Those constants are designed
	 * to match the values of the same constants on {@link java.sql.Connection}.
	 * 必须返回在{@link TransactionDefinition this interface}上定义的{@code ISOLATION_XXX}常量之一。
	 * 这些常量被设计成与{@link java.sql.Connection}相匹配
	 * <p>Exclusively designed for use with {@link #PROPAGATION_REQUIRED} or
	 * {@link #PROPAGATION_REQUIRES_NEW} since it only applies to newly started
	 * transactions.
	 * 专门设计用于{@link#PROPAGATION_REQUIRED}或{@link#PROPAGATION_REQUIRED_NEW}，因为它只适用于新启动的事务。
	 * Consider switching the "validateExistingTransactions" flag to
	 * "true" on your transaction manager if you'd like isolation level declarations
	 * to get rejected when participating in an existing transaction with a different
	 * isolation level.
	 * 如果您希望在参与具有不同隔离级别的现有事务时拒绝隔离级别声明，
	 * 请考虑将事务管理器上的“validateExistingTransactions”标志切换为“true”。
	 * <p>The default is {@link #ISOLATION_DEFAULT}. Note that a transaction manager
	 * that does not support custom isolation levels will throw an exception when
	 * given any other level than {@link #ISOLATION_DEFAULT}.
	 * 默认值是{@link#ISOLATION_default}。
	 * 请注意，不支持自定义隔离级别的事务管理器将在给定{@link#isolation_DEFAULT}之外的任何级别时引发异常。
	 * @return the isolation level
	 * @see #ISOLATION_DEFAULT
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setValidateExistingTransaction
	 */
	default int getIsolationLevel() {
		return ISOLATION_DEFAULT;
	}

	/**
	 * Return the transaction timeout.
	 * 返回事务超时时间。
	 * <p>Must return a number of seconds, or {@link #TIMEOUT_DEFAULT}.
	 * <p>Exclusively designed for use with {@link #PROPAGATION_REQUIRED} or
	 * {@link #PROPAGATION_REQUIRES_NEW} since it only applies to newly started
	 * transactions.
	 * 必须返回秒数，或{@link#TIMEOUT_DEFAULT}。
	 * 专门设计用于{@link#PROPAGATION_REQUIRED}或{@link#PROPAGATION_REQUIRED_NEW}，因为它只适用于新启动的事务。
	 * <p>Note that a transaction manager that does not support timeouts will throw
	 * an exception when given any other timeout than {@link #TIMEOUT_DEFAULT}.
	 * <p>The default is {@link #TIMEOUT_DEFAULT}.
	 * 请注意，不支持超时的事务管理器将在给定{@link#timeout_DEFAULT}之外的任何其他超时时抛出异常。
	 * 默认值是{@link#TIMEOUT_default}
	 * @return the transaction timeout
	 */
	default int getTimeout() {
		return TIMEOUT_DEFAULT;
	}

	/**
	 * Return whether to optimize as a read-only transaction.
	 * 返回是否作为只读事务进行优化。
	 * <p>The read-only flag applies to any transaction context, whether backed
	 * by an actual resource transaction ({@link #PROPAGATION_REQUIRED}/
	 * {@link #PROPAGATION_REQUIRES_NEW}) or operating non-transactionally at
	 * the resource level ({@link #PROPAGATION_SUPPORTS}). In the latter case,
	 * the flag will only apply to managed resources within the application,
	 * such as a Hibernate {@code Session}.
	 * 只读标志适用于任何事务上下文，无论是由实际的资源事务（{@link#PROPAGATION_REQUIRED}\{@link#PROPAGATION_REQUIRED_NEW}）支持，
	 * 还是以非事务方式在资源级别（{@link#PROPAGATION_SUPPORTS}）
	 * 在后一种情况下，该标志将只应用于应用程序中的托管资源，例如Hibernate{@code Session}。
	 * <p>This just serves as a hint for the actual transaction subsystem;
	 * it will <i>not necessarily</i> cause failure of write access attempts.
	 * 这只是对实际事务子系统的提示；它不一定会导致写访问尝试失败
	 * A transaction manager which cannot interpret the read-only hint will
	 * <i>not</i> throw an exception when asked for a read-only transaction.
	 * 一个不能解释只读事务提示的事务管理器，当被请求一个只读事务时，不会抛出异常
	 * @return {@code true} if the transaction is to be optimized as read-only
	 * ({@code false} by default)
	 * @see org.springframework.transaction.support.TransactionSynchronization#beforeCommit(boolean)
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isCurrentTransactionReadOnly()
	 */
	default boolean isReadOnly() {
		return false;
	}

	/**
	 * Return the name of this transaction. Can be {@code null}.
	 * 返回此事务的名称。可以是{@code null}
	 * <p>This will be used as the transaction name to be shown in a
	 * transaction monitor, if applicable (for example, WebLogic's).
	 * 如果合适，事务名字会再事务监视器被用到作展示，例如WebLogic
	 * <p>In case of Spring's declarative transactions, the exposed name will be
	 * the {@code fully-qualified class name + "." + method name} (by default).
	 * 对于Spring的声明性事务，公开的名称将是{@code完全限定类名+“.”+方法名}（默认情况下）。
	 * @return the name of this transaction ({@code null} by default}
	 * @see org.springframework.transaction.interceptor.TransactionAspectSupport
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#getCurrentTransactionName()
	 */
	@Nullable
	default String getName() {
		return null;
	}


	// Static builder methods

	/**
	 * Return an unmodifiable {@code TransactionDefinition} with defaults.
	 * 使用默认值返回不可修改的{@code TransactionDefinition}。
	 * <p>For customization purposes, use the modifiable 出于自定义目的，请使用可修改的
	 * {@link org.springframework.transaction.support.DefaultTransactionDefinition}
	 * instead.
	 * @since 5.2
	 */
	static TransactionDefinition withDefaults() {
		return StaticTransactionDefinition.INSTANCE;
	}

}
