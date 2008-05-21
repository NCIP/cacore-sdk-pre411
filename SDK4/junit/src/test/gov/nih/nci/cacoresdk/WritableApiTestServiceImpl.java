package test.gov.nih.nci.cacoresdk;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WritableApiTestServiceImpl implements WritableApiTestDAO {
	private WritableApplicationService appService ;
	
	public WritableApiTestServiceImpl(){
		try {
			appService = (WritableApplicationService) ApplicationServiceProvider.getApplicationService();
		} catch (Exception e) {
			throw new RuntimeException("error loading application service", e);
		}
	}
	
	protected ApplicationService getApplicationService() {
		return appService;
	}

	public static String getTestCaseName() {
		return "SDK Base Test Case";
	}
	
	public void save(Object obj) {
		final InsertExampleQuery sdkQuery = new InsertExampleQuery(obj);
		new BaseUtilWrapper() {

			@Override
			public List execute() throws Exception {
				appService.executeQuery(sdkQuery);
				return null;
			}
		}.executeLogic();
	}

	public void update(Object obj) {
		final UpdateExampleQuery sdkQuery = new UpdateExampleQuery(obj);
		new BaseUtilWrapper() {

			@Override
			public List execute() throws Exception {
				appService.executeQuery(sdkQuery);
				return null;
			}
		}.executeLogic();
	}

	public void delete(Object obj) {
		final DeleteExampleQuery sdkQuery = new DeleteExampleQuery(obj);
		new BaseUtilWrapper() {

			@Override
			public List execute() throws Exception {
				appService.executeQuery(sdkQuery);
				return null;
			}
		}.executeLogic();
	}
	
	@SuppressWarnings("unchecked")
	public Object getObject(final Class klass,final int id) {
		final List resultList = new ArrayList();
		final Object instance = getObjectInstance(klass, id);

		new BaseUtilWrapper() {

			@Override
			public List execute() throws Exception {
				List list=appService.search(klass, instance);
				resultList.addAll(list);
				return resultList;
			}

		}.executeLogic();
		
		Object result = null;
		if (resultList != null && resultList.size() > 0) {
			result = resultList.get(0);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyCollection(final Class klass,final int id,final String lazyCollectionName) {
		final Object instance =getObject(klass,id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyObject(Class klass,int id,String lazyObjName) {
		final Object instance =getObject(klass,id);
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndMultipleLazyObjects(Class klass,int id,String lazyObjName1,String lazyObjName2) {
		final Object instance =getObject(klass,id);
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private abstract class BaseUtilWrapper {
		public abstract List execute() throws Exception;

		public void executeLogic() {
			try {
				execute();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object getObjectInstance(final Class klass, final int id) {
		final List list = new ArrayList();
		new BaseUtilWrapper() {

			@Override
			public List execute() throws Exception {
				Class classDefinition = Class.forName(klass.getName());
				Object instance = classDefinition.newInstance();
				Class[] parameterTypes = new Class[] { Integer.class };
				Method method = klass.getMethod("setId", parameterTypes);
				method.invoke(instance, id);

				list.add(instance);
				return list;
			}
		}.executeLogic();
		return list.get(0);
	}
}
