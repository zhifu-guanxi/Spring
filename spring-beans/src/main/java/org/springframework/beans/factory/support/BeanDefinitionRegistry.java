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

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

/**
 * Interface for registries that hold bean definitions, for example RootBeanDefinition
 * and ChildBeanDefinition instances.
 * 用于保存bean定义的注册表的接口，例如RootBeanDefinition和ChildBeanDefinition实例。
 * Typically implemented by BeanFactories that
 * internally work with the AbstractBeanDefinition hierarchy.
 * 通常由内部使用AbstractBeanDefinition层次结构的BeanFactory实现。
 *
 * <p>This is the only interface in Spring's bean factory packages that encapsulates
 * <i>registration</i> of bean definitions.
 * 这是Spring的bean工厂包中封装bean定义的<i>registration<i>的唯一接口。
 * The standard BeanFactory interfaces
 * only cover access to a <i>fully configured factory instance</i>.
 * 标准BeanFactory接口只覆盖对完全配置的工厂实例的访问
 *
 * <p>Spring's bean definition readers expect to work on an implementation of this
 * interface.
 * Spring的bean定义阅读器希望能够处理这个接口的实现。
 * Known implementors within the Spring core are DefaultListableBeanFactory
 * and GenericApplicationContext.
 * Spring核心中的已知实现者是DefaultListableBeanFactory和GenericApplicationContext。
 *
 * @author Juergen Hoeller
 * @since 26.11.2003
 * @see org.springframework.beans.factory.config.BeanDefinition
 * @see AbstractBeanDefinition
 * @see RootBeanDefinition
 * @see ChildBeanDefinition
 * @see DefaultListableBeanFactory
 * @see org.springframework.context.support.GenericApplicationContext
 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
 * @see PropertiesBeanDefinitionReader
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

	/**
	 * Register a new bean definition with this registry.
	 * 向这个注册表注册一个新的bean定义。
	 * Must support RootBeanDefinition and ChildBeanDefinition.
	 * 必须支持RootBeanDefinition和ChildBeanDefinition。
	 * @param beanName the name of the bean instance to register 将要注册的bean实例名字
	 * @param beanDefinition definition of the bean instance to register 要注册的beanDefinition
	 * @throws BeanDefinitionStoreException if the BeanDefinition is invalid 如果beanDefinition是无效的
	 * @throws BeanDefinitionOverrideException if there is already a BeanDefinition
	 * for the specified bean name and we are not allowed to override it
	 * 如果已经有一个同名的bean并且不允许重写
	 * @see GenericBeanDefinition
	 * @see RootBeanDefinition
	 * @see ChildBeanDefinition
	 */
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException;

	/**
	 * Remove the BeanDefinition for the given name.
	 * 根据给定的bean名字移除BeanDefinition
	 * @param beanName the name of the bean instance to register 注册的bean实例名称
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition 如果没有这个bean抛出异常
	 */
	void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Return the BeanDefinition for the given bean name.
	 * 根据给定的bean名字返回BeanDefinition
	 * @param beanName name of the bean to find a definition for 注册的bean实例名称
	 * @return the BeanDefinition for the given name (never {@code null}) 返回的BeanDefinition不会为null
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition 如果没有这个bean抛出异常
	 */
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Check if this registry contains a bean definition with the given name.
	 * 检查此注册表是否包含具有给定名称的bean定义。
	 * @param beanName the name of the bean to look for
	 * @return if this registry contains a bean definition with the given name
	 */
	boolean containsBeanDefinition(String beanName);

	/**
	 * Return the names of all beans defined in this registry.
	 * 返回所有已定义bean的名称
	 * @return the names of all beans defined in this registry,
	 * or an empty array if none defined
	 */
	String[] getBeanDefinitionNames();

	/**
	 * Return the number of beans defined in the registry.
	 * 返回已经注册的bean数量
	 * @return the number of beans defined in the registry
	 */
	int getBeanDefinitionCount();

	/**
	 * Determine whether the given bean name is already in use within this registry,
	 * 确定给定的bean名称是否已在此注册表中使用，
	 * i.e. whether there is a local bean or alias registered under this name.
	 * 也就是说，是否有本地bean或别名注册在此名称下。
	 * @param beanName the name to check
	 * @return whether the given bean name is already in use
	 */
	boolean isBeanNameInUse(String beanName);

}
