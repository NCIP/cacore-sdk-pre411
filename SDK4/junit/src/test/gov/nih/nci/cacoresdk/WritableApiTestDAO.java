package test.gov.nih.nci.cacoresdk;

public interface WritableApiTestDAO {

	public void save(Object obj);

	public void update(Object obj);

	public void delete(Object obj);

	@SuppressWarnings("unchecked")
	public Object getObject(Class klass, int id);

	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyCollection(Class klass,int id,String lazyCollectionName);
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndLazyObject(Class klass, int id,String lazyObjName);
	
	@SuppressWarnings("unchecked")
	public Object getObjectAndMultipleLazyObjects(Class klass, int id,String lazyObjName1,String lazyObjName2);

}
