<class-file>
 <package>${utils.getNamespaceName(utils.getPackage(utils.getModel(modelElement), basePkg), modelElement)}</package>
 <class>
  <modifiers>public</modifiers>
  <name>${modelElement.getName()}</name>
  <#if utils.getSuperClass(modelElement)?exists>
  <extends>${utils.getSuperClass(modelElement).getName()}</extends>
  </#if>
  <body>
   <#list utils.getAttributes(modelElement) as att>
    <${"attribute"} type="${att.getType().getName()}" name="${att.getName()}">
     <modifiers>private</modifiers>
    </attribute>
   </#list>
   <#list utils.getAttributes(modelElement) as att>
    <method name="set${att.getName()?cap_first}">
     <modifiers>public</modifiers>
     <params>
      <param type="${att.getType().getName()}" 
             name="p_${att.getName()}"/>
     </params>
     <body>
      ${att.getName()} = p_${att.getName()};
     </body>
    </method>
    <method name="get${att.getName()?cap_first}" 
            returnType="${att.getType().getName()}">
     <modifiers>public</modifiers>
     <body>
      return ${att.getName()};
     </body>
    </method>
   </#list>
   <method name="doSomething">
    <modifiers>public</modifiers>
	<body>
	 System.out.println("Something");
	</body>
   </method>
  </body>
 </class>
</class-file>
