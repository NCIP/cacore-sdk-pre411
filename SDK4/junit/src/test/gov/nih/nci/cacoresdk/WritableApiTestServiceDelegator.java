package test.gov.nih.nci.cacoresdk;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WritableApiTestServiceDelegator {

	private static ClassPathXmlApplicationContext applicationContext=null;
	
	WritableApiTestDAO appservice;

	public WritableApiTestServiceDelegator() {
		loadWritableApiTestDAO();
		//loadWritableApiApplicationService();
	}

	public void loadWritableApiTestDAO() {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext("/application-config-test.xml");
		}
		appservice = (WritableApiTestDAOImpl) applicationContext.getBean("WritableApiTestDAO");
	}
	
	public void loadWritableApiApplicationService() {
		appservice=new WritableApiTestServiceImpl();
	}
	
	public void save(Object obj) {
		appservice.save(obj);
	}

	public void update(Object obj) {
		appservice.update(obj);
	}

	public void delete(Object obj) {
		appservice.delete(obj);
	}

	@SuppressWarnings("unchecked")
	public Object getObject(Class klass, int id) {
		Object obj = appservice.getObject(klass, id);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyCollection(Class klass, int id,String lazyCollectionName) {
		return appservice.getObjectAndLazyCollection(klass, id, lazyCollectionName);
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyObject(Class klass,int id,String lazyObjName) {
		return appservice.getObjectAndLazyObject(klass, id, lazyObjName);
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndMultipleLazyObjects(Class klass, int id,
			String lazyObjName1, String lazyObjName2) {
		return appservice.getObjectAndMultipleLazyObjects(klass, id, lazyObjName1, lazyObjName2);
	}
}
