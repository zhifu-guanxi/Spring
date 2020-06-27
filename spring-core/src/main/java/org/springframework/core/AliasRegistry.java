/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.core;

/**
 * Common interface for managing aliases. Serves as super-interface for
 * 管理别名的通用接口。作为BeanDefinitionRegistry的父接口
 * {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 */
public interface AliasRegistry {

	/**
	 * Given a name, register an alias for it.
	 * 给定一个名称，为它注册一个别名。
	 * @param name the canonical name 正名
	 * @param alias the alias to be registered 想要注册的别名
	 * @throws IllegalStateException if the alias is already in use
	 * and may not be overridden
	 * 如果别名已经被注册，则不会被重写，抛出异常
	 */
	void registerAlias(String name, String alias);

	/**
	 * Remove the specified alias from this registry.
	 * 从此注册表中删除指定的别名。
	 * @param alias the alias to remove 要删除的别名
	 * @throws IllegalStateException if no such alias was found
	 * 如果没有此别名，则抛出异常
	 */
	void removeAlias(String alias);

	/**
	 * Determine whether this given name is defines as an alias
	 * (as opposed to the name of an actually registered component).
	 * 确定一个名字是否已被定义为别名（而不是判断是注册了的组件名称）
	 * @param name the name to check 需要检查的名字
	 * @return whether the given name is an alias 给定的名字是否是别名
	 */
	boolean isAlias(String name);

	/**
	 * Return the aliases for the given name, if defined.
	 * 如果已定义，则返回给定名称的别名。
	 * @param name the name to check for aliases 传入正名
	 * @return the aliases, or an empty array if none 返回定义的别名，如果没有则是空数组
	 */
	String[] getAliases(String name);

}
