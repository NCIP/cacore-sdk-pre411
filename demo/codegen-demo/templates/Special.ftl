<#assign rootEl = helper.buildJDOMElement(input)>
<#assign methEl = helper.selectElement("class/body/method[@name='doSomething']", rootEl)/>
<#assign newBody>
 System.out.println("Something ELSE!!!");
</#assign>
<#assign dummy = methEl.getChild("body").setText(newBody)/>
${helper.outputString(rootEl)}