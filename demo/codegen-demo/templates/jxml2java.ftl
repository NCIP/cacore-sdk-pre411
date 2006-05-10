<#assign rootEl = helper.buildJDOMElement(input)/>
package ${helper.selectElement("package", rootEl).getTextNormalize().trim()};

<#-- Insert imports -->
<#if helper.selectElement("imports", rootEl)?exists>
 <#compress>
  <#list helper.selectElements("imports/item", rootEl) as item>
   import ${item.getTextNormalize().trim()};
  </#list>
 </#compress> 
</#if>

<#-- Insert class javadoc -->
<#if helper.selectElement("javadoc", rootEl)?exists>
<#compress>
${helper.selectElement("javadoc", rootEl).getText()}
</#compress>
</#if>

<#-- Class declaration -->
<#if rootEl.getChild("class")?exists>
   <#assign classEl = rootEl.getChild("class")/>
<#else>
   <#assign classEl = rootEl.getChild("interface")/>
</#if>

<#nt/>
<#if classEl.getChild("modifiers")?exists>${classEl.getChild("modifiers").getTextNormalize().trim()} </#if>${classEl.getName()} ${classEl.getChild("name").getTextNormalize().trim()}
<#if classEl.getChild("extends")?exists>
   extends ${classEl.getChild("extends").getTextNormalize().trim()}
</#if>
<#if classEl.getChild("implements")?exists>
   implements ${classEl.getChild("implements").getTextNormalize().trim()}
</#if>
{

<#-- Insert attribues -->
<#if helper.selectElements("body/attribute", classEl)?exists>
 <#list helper.selectElements("body/attribute", classEl) as att>
  <#if att.getChild("javadoc")?exists>
   ${att.getChild("javadoc").getText()}
  </#if>
  <#if att.getChild("modifiers")?exists>${att.getChild("modifiers").getTextNormalize().trim()} </#if>${att.getAttributeValue("type")} ${att.getAttributeValue("name")}<#if att.getChild("initial-value")?exists> = ${att.getChild("initial-value").getTextNormalize().trim()}</#if>;
  
 </#list>
</#if>

<#-- Insert constructors -->
<#if helper.selectElements("body/constructor", classEl)?exists>
 <#list helper.selectElements("body/constructor", classEl) as con>
  <#if con.getChild("javadoc")?exists>
   ${con.getChild("javadoc").getText()}
  </#if>
  <#if con.getChild("modifiers")?exists>${con.getChild("modifiers").getTextNormalize().trim()} </#if>${classEl.getChild("name").getTextNormalize().trim()}(<#if con.getChild("params")?exists><#list helper.selectElements("params/param", con) as param>${param.getAttributeValue("type")} ${param.getAttributeValue("name")}<#if param_has_next>, </#if></#list></#if>)<#if con.getChild("throws")?exists> throws ${con.getChild("throws").getTextNormalize().trim()}</#if>{
     ${con.getChild("body").getText()}
  }
 </#list>
</#if>

<#-- Insert methods -->
<#if helper.selectElements("body/method", classEl)?exists>
 <#list helper.selectElements("body/method", classEl) as meth>
  <#if meth.getChild("javadoc")?exists>
   ${meth.getChild("javadoc").getText()}
  </#if>
  <#if meth.getChild("modifiers")?exists>${meth.getChild("modifiers").getTextNormalize().trim()} </#if><#if meth.getAttributeValue("returnType")?exists>${meth.getAttributeValue("returnType")} <#else>void </#if>${meth.getAttributeValue("name")}(<#if meth.getChild("params")?exists><#list helper.selectElements("params/param", meth) as param>${param.getAttributeValue("type")} ${param.getAttributeValue("name")}<#if param_has_next>, </#if></#list></#if>)<#if meth.getChild("throws")?exists> throws ${meth.getChild("throws").getTextNormalize().trim()}</#if>
  <#if meth.getParent().getParent().getName() = "class">
  {
     ${meth.getChild("body").getText()}
  }
  <#else>
  ;
  </#if>
 </#list>
</#if>
}
