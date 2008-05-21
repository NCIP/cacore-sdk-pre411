package test.gov.nih.nci.codegen.validator;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static junit.framework.Assert.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.codegen.validator.WritableApiValidator;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WritableApiValidatorTest{

	private WritableApiValidator validator;
	private UMLModel model;
	private UMLAssociation cascadeAssociation;
	private UMLAssociationEnd thisEnd;
	private UMLAssociationEnd otherEnd;
	private TransformerUtils transformerUtils;
	private UMLClass klass;
	private UMLClass assocKlass;
	
	public static String getTestCaseName() {
		return "WritableApiValidator Test Case";
	}
	
	@Before
	public void setUp() {
		validator = new WritableApiValidator();
		model = createMock(UMLModel.class);
		klass= createMock(UMLClass.class);
		assocKlass= createMock(UMLClass.class);
		otherEnd= createMock(UMLAssociationEnd.class);
		thisEnd= createMock(UMLAssociationEnd.class);
		transformerUtils=createMock(TransformerUtils.class);
		cascadeAssociation = createMock(UMLAssociation.class);
		
		validator.setTransformerUtils(transformerUtils);
	}

	@After
	public void tearDown() {
		validator = null;
	}

	@Test
	public void validateWritableApi() throws Exception {
		setMany2OneCascadeAssocExpectations();
		setBidirectionalImplicitMappingExpectations();

		GeneratorErrors errors = validator.validate(model);
		assertEquals(2, errors.getErrors().size());
		assertEquals("[gov.nih.nci.codegen.validator.WritableApiValidator: Cascade-style setting delete-orphan is not supported on the Many-to-One association between currentklass and assocklass.  Please change the NCI_CASCADE_ASSOCIATION Tag Value delete-orphan from the association.]",errors.getErrors().toString());
		verifyAndReset();
	}

	
	private void setBidirectionalImplicitMappingExpectations() throws GenerationException {
		String roleName = "roleName";
		Collection<UMLClass> parentClasses = new ArrayList<UMLClass>();
		parentClasses.add(klass);
		
		Set<UMLAssociation>cascadeAssociations = new HashSet<UMLAssociation>();
		cascadeAssociations.add(cascadeAssociation);
		List<UMLAssociationEnd>assocEnds = new ArrayList<UMLAssociationEnd>();
		assocEnds.add(thisEnd);
		assocEnds.add(otherEnd);
		
		expect(transformerUtils.getAllParentClasses(model)).andReturn(parentClasses);
		expect(klass.getAssociations()).andReturn(cascadeAssociations);
		cascadeAssociation=cascadeAssociations.iterator().next();
		expect(cascadeAssociation.getAssociationEnds()).andReturn(assocEnds);
		expect(transformerUtils.getThisEnd(klass, assocEnds)).andReturn(thisEnd);
		expect(transformerUtils.getOtherEnd(klass, assocEnds)).andReturn(otherEnd);
		expect(otherEnd.isNavigable()).andReturn(true);
		expect(transformerUtils.isMany2Any(thisEnd, otherEnd)).andReturn(true);
		expect(thisEnd.isNavigable()).andReturn(true);
		expect(otherEnd.getUMLElement()).andReturn(assocKlass);
		expect(transformerUtils.getFQCN(klass)).andReturn("currentklass");
		expect(transformerUtils.getFQCN(assocKlass)).andReturn("assocklass");
		expect(otherEnd.getRoleName()).andReturn(roleName);
		expect(transformerUtils.findCascadeStyle(klass, roleName,cascadeAssociation)).andReturn("delete-orphan");
		expect(transformerUtils.isImplicitParent(klass)).andReturn(false);
		
		expect(transformerUtils.isAny(thisEnd, otherEnd)).andReturn(false);
		
		replay(klass);
		replay(cascadeAssociation);
		replay(transformerUtils);
		replay(thisEnd);
		replay(otherEnd);
	}
	
	private void setMany2OneCascadeAssocExpectations() throws GenerationException {
		String roleName = "roleName";
		Collection<UMLClass> parentClasses = new ArrayList<UMLClass>();
		parentClasses.add(klass);
		
		Set<UMLAssociation>cascadeAssociations = new HashSet<UMLAssociation>();
		cascadeAssociations.add(cascadeAssociation);
		List<UMLAssociationEnd>assocEnds = new ArrayList<UMLAssociationEnd>();
		assocEnds.add(thisEnd);
		assocEnds.add(otherEnd);
		
		expect(transformerUtils.getAllParentClasses(model)).andReturn(parentClasses);
		expect(klass.getAssociations()).andReturn(cascadeAssociations);
		cascadeAssociation=cascadeAssociations.iterator().next();
		expect(cascadeAssociation.getAssociationEnds()).andReturn(assocEnds);
		expect(transformerUtils.getThisEnd(klass, assocEnds)).andReturn(thisEnd);
		expect(transformerUtils.getOtherEnd(klass, assocEnds)).andReturn(otherEnd);
		expect(otherEnd.isNavigable()).andReturn(true);
		expect(transformerUtils.isMany2One(thisEnd, otherEnd)).andReturn(true);
		expect(otherEnd.getUMLElement()).andReturn(assocKlass);
		expect(transformerUtils.getFQCN(klass)).andReturn("currentklass");
		expect(transformerUtils.getFQCN(assocKlass)).andReturn("assocklass");
		expect(otherEnd.getRoleName()).andReturn(roleName);
		expect(transformerUtils.findCascadeStyle(klass, roleName,cascadeAssociation)).andReturn("delete-orphan");
		expect(transformerUtils.isImplicitParent(klass)).andReturn(false);

	}

	private void verifyAndReset() {
		verify(transformerUtils);
		verify(klass);
		verify(otherEnd);
		verify(thisEnd);
		verify(cascadeAssociation);

		reset(transformerUtils);
		reset(klass);
		reset(otherEnd);
		reset(thisEnd);
		reset(cascadeAssociation);
	}
}
