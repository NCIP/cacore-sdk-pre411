package test.gov.nih.nci.cacoresdk;

import junit.framework.TestCase;
public class SDKWritableApiBaseTest extends TestCase{
	
	private WritableApiTestServiceDelegator serviceDelegator;
	
	protected void setUp() throws Exception {
		super.setUp();
		serviceDelegator=new WritableApiTestServiceDelegator();
	}	
	
	protected void tearDown() throws Exception {
		serviceDelegator = null;
		super.tearDown();
	}

	public static String getTestCaseName() {
		return "SDK Base Test Case";
	}
	
	public void save(Object obj) {
		serviceDelegator.save(obj);
	}

	public void update(Object obj) {
		serviceDelegator.update(obj);
	}

	public void delete(Object obj) {
		serviceDelegator.delete(obj);
	}
	
	@SuppressWarnings("unchecked")
	public Object getObject(final Class klass,final int id) {
		return serviceDelegator.getObject(klass, id);
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyCollection(final Class klass,final int id,final String lazyCollectionName) {
		return serviceDelegator.getObjectAndLazyCollection(klass,id,lazyCollectionName);
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyObject(Class klass,int id,String lazyObjName) {
		return serviceDelegator.getObjectAndLazyObject(klass, id, lazyObjName);
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndMultipleLazyObjects(Class klass,int id,String lazyObjName1,String lazyObjName2) {
		return serviceDelegator.getObjectAndMultipleLazyObjects(klass, id, lazyObjName1, lazyObjName2);
	}
}
