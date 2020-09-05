/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.context.annotation;

/**
 * A {@link Condition} that offers more fine-grained control when used with
 * {@code @Configuration}. Allows certain {@link Condition Conditions} to adapt when they match
 * based on the configuration phase.
 * 当与{@code@Configuration}一起使用时，{@link Condition}提供更细粒度的控制。
 * 允许某些{@link Condition}根据配置阶段进行匹配时进行调整。
 * For example, a condition that checks if a bean
 * has already been registered might choose to only be evaluated during the
 * {@link ConfigurationPhase#REGISTER_BEAN REGISTER_BEAN} {@link ConfigurationPhase}.
 * 例如，检查某个bean是否已经注册的条件可能选择只在{@link ConfigurationPhase#REGISTER_BEAN}期间计算。
 *
 * @author Phillip Webb
 * @since 4.0
 * @see Configuration
 */
public interface ConfigurationCondition extends Condition {

	/**
	 * Return the {@link ConfigurationPhase} in which the condition should be evaluated.
	 * Return the {@link ConfigurationPhase} in which the condition should be evaluated.
	 */
	ConfigurationPhase getConfigurationPhase();


	/**
	 * The various configuration phases where the condition could be evaluated.
	 * 可以评估条件的各种配置阶段。
	 */
	enum ConfigurationPhase {

		/**
		 * The {@link Condition} should be evaluated as a {@code @Configuration}
		 * class is being parsed.
		 * {@link Condition}应在{@code@Configuration}类被解析时进行计算。
		 * <p>If the condition does not match at this point, the {@code @Configuration}
		 * class will not be added.
		 * 如果此时条件不匹配，则不会添加{@code@Configuration}类。
		 */
		PARSE_CONFIGURATION,

		/**
		 * The {@link Condition} should be evaluated when adding a regular
		 * (non {@code @Configuration}) bean.
		 * 当添加常规（非{@code@Configuration}）bean时，应该计算{@link Condition}。
		 * The condition will not prevent {@code @Configuration} classes from being added.
		 * 该条件不会阻止添加{@code@Configuration}类。
		 * <p>At the time that the condition is evaluated, all {@code @Configuration}s
		 * will have been parsed.
		 * 在计算条件时，所有{@code@Configuration}都将被解析。
		 */
		REGISTER_BEAN
	}

}
