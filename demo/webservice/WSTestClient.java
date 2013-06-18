/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

import java.net.URL;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import gov.nih.nci.cabio.domain.*;
import gov.nih.nci.cabio.domain.ws.*;

import gov.nih.nci.cadsr.domain.*;
import gov.nih.nci.cadsr.domain.ws.*;

import gov.nih.nci.camod.domain.*;
import gov.nih.nci.camod.domain.ws.*;

import gov.nih.nci.common.domain.*;
import gov.nih.nci.common.domain.ws.*;

import gov.nih.nci.common.provenance.domain.*;
import gov.nih.nci.common.provenance.domain.ws.*;


import java.util.HashMap;

public class WSTestClient
{

	public static void main(String [] args) throws Exception {

		Service  service = new Service();
		Call     call    = (Call) service.createCall();

		/***************************************************************************************************************/

		QName qnAbstractCancerModel = new QName("urn:ws.domain.camod.nci.nih.gov", "AbstractCancerModelImpl");
		call.registerTypeMapping(AbstractCancerModelImpl.class, qnAbstractCancerModel,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AbstractCancerModelImpl.class, qnAbstractCancerModel),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AbstractCancerModelImpl.class, qnAbstractCancerModel));
		QName qnXenograft = new QName("urn:ws.domain.camod.nci.nih.gov", "XenograftImpl");
		call.registerTypeMapping(XenograftImpl.class, qnXenograft,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(XenograftImpl.class, qnXenograft),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(XenograftImpl.class, qnXenograft));
		QName qnAnimalModel = new QName("urn:ws.domain.camod.nci.nih.gov", "AnimalModelImpl");
		call.registerTypeMapping(AnimalModelImpl.class, qnAnimalModel,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AnimalModelImpl.class, qnAnimalModel),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AnimalModelImpl.class, qnAnimalModel));
		QName qnAdministeredComponentClassSchemeItem = new QName("urn:ws.domain.cadsr.nci.nih.gov", "AdministeredComponentClassSchemeItemImpl");
		call.registerTypeMapping(AdministeredComponentClassSchemeItemImpl.class, qnAdministeredComponentClassSchemeItem,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AdministeredComponentClassSchemeItemImpl.class, qnAdministeredComponentClassSchemeItem),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AdministeredComponentClassSchemeItemImpl.class, qnAdministeredComponentClassSchemeItem));
		QName qnAdministeredComponent = new QName("urn:ws.domain.cadsr.nci.nih.gov", "AdministeredComponentImpl");
		call.registerTypeMapping(AdministeredComponentImpl.class, qnAdministeredComponent,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AdministeredComponentImpl.class, qnAdministeredComponent),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AdministeredComponentImpl.class, qnAdministeredComponent));
		QName qnValidValue = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ValidValueImpl");
		call.registerTypeMapping(ValidValueImpl.class, qnValidValue,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ValidValueImpl.class, qnValidValue),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ValidValueImpl.class, qnValidValue));
		QName qnRepresentation = new QName("urn:ws.domain.cadsr.nci.nih.gov", "RepresentationImpl");
		call.registerTypeMapping(RepresentationImpl.class, qnRepresentation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(RepresentationImpl.class, qnRepresentation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(RepresentationImpl.class, qnRepresentation));
		QName qnConcept = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ConceptImpl");
		call.registerTypeMapping(ConceptImpl.class, qnConcept,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ConceptImpl.class, qnConcept),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ConceptImpl.class, qnConcept));
		QName qnQuestion = new QName("urn:ws.domain.cadsr.nci.nih.gov", "QuestionImpl");
		call.registerTypeMapping(QuestionImpl.class, qnQuestion,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(QuestionImpl.class, qnQuestion),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(QuestionImpl.class, qnQuestion));
		QName qnObjectClassRelationship = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ObjectClassRelationshipImpl");
		call.registerTypeMapping(ObjectClassRelationshipImpl.class, qnObjectClassRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ObjectClassRelationshipImpl.class, qnObjectClassRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ObjectClassRelationshipImpl.class, qnObjectClassRelationship));
		QName qnProperty = new QName("urn:ws.domain.cadsr.nci.nih.gov", "PropertyImpl");
		call.registerTypeMapping(PropertyImpl.class, qnProperty,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PropertyImpl.class, qnProperty),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PropertyImpl.class, qnProperty));
		QName qnClassificationScheme = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ClassificationSchemeImpl");
		call.registerTypeMapping(ClassificationSchemeImpl.class, qnClassificationScheme,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ClassificationSchemeImpl.class, qnClassificationScheme),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ClassificationSchemeImpl.class, qnClassificationScheme));
		QName qnModule = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ModuleImpl");
		call.registerTypeMapping(ModuleImpl.class, qnModule,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ModuleImpl.class, qnModule),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ModuleImpl.class, qnModule));
		QName qnConceptualDomain = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ConceptualDomainImpl");
		call.registerTypeMapping(ConceptualDomainImpl.class, qnConceptualDomain,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ConceptualDomainImpl.class, qnConceptualDomain),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ConceptualDomainImpl.class, qnConceptualDomain));
		QName qnObjectClass = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ObjectClassImpl");
		call.registerTypeMapping(ObjectClassImpl.class, qnObjectClass,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ObjectClassImpl.class, qnObjectClass),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ObjectClassImpl.class, qnObjectClass));
		QName qnInstruction = new QName("urn:ws.domain.cadsr.nci.nih.gov", "InstructionImpl");
		call.registerTypeMapping(InstructionImpl.class, qnInstruction,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(InstructionImpl.class, qnInstruction),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(InstructionImpl.class, qnInstruction));
		QName qnValueDomain = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ValueDomainImpl");
		call.registerTypeMapping(ValueDomainImpl.class, qnValueDomain,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ValueDomainImpl.class, qnValueDomain),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ValueDomainImpl.class, qnValueDomain));

		QName qnAgent = new QName("urn:ws.domain.cabio.nci.nih.gov", "AgentImpl");
		call.registerTypeMapping(AgentImpl.class, qnAgent,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AgentImpl.class, qnAgent),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AgentImpl.class, qnAgent));
		QName qnAnomaly = new QName("urn:ws.domain.cabio.nci.nih.gov", "AnomalyImpl");
		call.registerTypeMapping(AnomalyImpl.class, qnAnomaly,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AnomalyImpl.class, qnAnomaly),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AnomalyImpl.class, qnAnomaly));
		QName qnApprovalStatus = new QName("urn:ws.domain.camod.nci.nih.gov", "ApprovalStatusImpl");
		call.registerTypeMapping(ApprovalStatusImpl.class, qnApprovalStatus,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ApprovalStatusImpl.class, qnApprovalStatus),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ApprovalStatusImpl.class, qnApprovalStatus));
		QName qnAvailability = new QName("urn:ws.domain.camod.nci.nih.gov", "AvailabilityImpl");
		call.registerTypeMapping(AvailabilityImpl.class, qnAvailability,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(AvailabilityImpl.class, qnAvailability),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(AvailabilityImpl.class, qnAvailability));
		QName qnCancerModel = new QName("urn:ws.domain.camod.nci.nih.gov", "CancerModelImpl");
		call.registerTypeMapping(CancerModelImpl.class, qnCancerModel,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CancerModelImpl.class, qnCancerModel),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CancerModelImpl.class, qnCancerModel));
		QName qnCarcinogenicIntervention = new QName("urn:ws.domain.camod.nci.nih.gov", "CarcinogenicInterventionImpl");
		call.registerTypeMapping(CarcinogenicInterventionImpl.class, qnCarcinogenicIntervention,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CarcinogenicInterventionImpl.class, qnCarcinogenicIntervention),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CarcinogenicInterventionImpl.class, qnCarcinogenicIntervention));
		QName qnCellLine = new QName("urn:ws.domain.camod.nci.nih.gov", "CellLineImpl");
		call.registerTypeMapping(CellLineImpl.class, qnCellLine,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CellLineImpl.class, qnCellLine),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CellLineImpl.class, qnCellLine));
		QName qnChromosome = new QName("urn:ws.domain.cabio.nci.nih.gov", "ChromosomeImpl");
		call.registerTypeMapping(ChromosomeImpl.class, qnChromosome,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ChromosomeImpl.class, qnChromosome),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ChromosomeImpl.class, qnChromosome));
		QName qnClassificationSchemeItem = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ClassificationSchemeItemImpl");
		call.registerTypeMapping(ClassificationSchemeItemImpl.class, qnClassificationSchemeItem,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ClassificationSchemeItemImpl.class, qnClassificationSchemeItem),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ClassificationSchemeItemImpl.class, qnClassificationSchemeItem));
		QName qnClassificationSchemeItemRelationship = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ClassificationSchemeItemRelationshipImpl");
		call.registerTypeMapping(ClassificationSchemeItemRelationshipImpl.class, qnClassificationSchemeItemRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ClassificationSchemeItemRelationshipImpl.class, qnClassificationSchemeItemRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ClassificationSchemeItemRelationshipImpl.class, qnClassificationSchemeItemRelationship));
		QName qnClassificationSchemeRelationship = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ClassificationSchemeRelationshipImpl");
		call.registerTypeMapping(ClassificationSchemeRelationshipImpl.class, qnClassificationSchemeRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ClassificationSchemeRelationshipImpl.class, qnClassificationSchemeRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ClassificationSchemeRelationshipImpl.class, qnClassificationSchemeRelationship));
		QName qnClassSchemeClassSchemeItem = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ClassSchemeClassSchemeItemImpl");
		call.registerTypeMapping(ClassSchemeClassSchemeItemImpl.class, qnClassSchemeClassSchemeItem,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ClassSchemeClassSchemeItemImpl.class, qnClassSchemeClassSchemeItem),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ClassSchemeClassSchemeItemImpl.class, qnClassSchemeClassSchemeItem));
		QName qnClinicalTrialProtocol = new QName("urn:ws.domain.cabio.nci.nih.gov", "ClinicalTrialProtocolImpl");
		call.registerTypeMapping(ClinicalTrialProtocolImpl.class, qnClinicalTrialProtocol,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ClinicalTrialProtocolImpl.class, qnClinicalTrialProtocol),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ClinicalTrialProtocolImpl.class, qnClinicalTrialProtocol));
		QName qnClone = new QName("urn:ws.domain.cabio.nci.nih.gov", "CloneImpl");
		call.registerTypeMapping(CloneImpl.class, qnClone,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CloneImpl.class, qnClone),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CloneImpl.class, qnClone));
		QName qnCloneRelativeLocation = new QName("urn:ws.domain.cabio.nci.nih.gov", "CloneRelativeLocationImpl");
		call.registerTypeMapping(CloneRelativeLocationImpl.class, qnCloneRelativeLocation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CloneRelativeLocationImpl.class, qnCloneRelativeLocation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CloneRelativeLocationImpl.class, qnCloneRelativeLocation));
		QName qnComponentConcept = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ComponentConceptImpl");
		call.registerTypeMapping(ComponentConceptImpl.class, qnComponentConcept,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ComponentConceptImpl.class, qnComponentConcept),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ComponentConceptImpl.class, qnComponentConcept));
		QName qnConceptDerivationRule = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ConceptDerivationRuleImpl");
		call.registerTypeMapping(ConceptDerivationRuleImpl.class, qnConceptDerivationRule,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ConceptDerivationRuleImpl.class, qnConceptDerivationRule),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ConceptDerivationRuleImpl.class, qnConceptDerivationRule));
		QName qnConditionality = new QName("urn:ws.domain.camod.nci.nih.gov", "ConditionalityImpl");
		call.registerTypeMapping(ConditionalityImpl.class, qnConditionality,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ConditionalityImpl.class, qnConditionality),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ConditionalityImpl.class, qnConditionality));
		QName qnContactInfo = new QName("urn:ws.domain.camod.nci.nih.gov", "ContactInfoImpl");
		call.registerTypeMapping(ContactInfoImpl.class, qnContactInfo,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ContactInfoImpl.class, qnContactInfo),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ContactInfoImpl.class, qnContactInfo));
		QName qnContext = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ContextImpl");
		call.registerTypeMapping(ContextImpl.class, qnContext,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ContextImpl.class, qnContext),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ContextImpl.class, qnContext));
		QName qnCytoband = new QName("urn:ws.domain.cabio.nci.nih.gov", "CytobandImpl");
		call.registerTypeMapping(CytobandImpl.class, qnCytoband,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CytobandImpl.class, qnCytoband),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CytobandImpl.class, qnCytoband));
		QName qnDatabaseCrossReference = new QName("urn:ws.domain.common.nci.nih.gov", "DatabaseCrossReferenceImpl");
		call.registerTypeMapping(DatabaseCrossReferenceImpl.class, qnDatabaseCrossReference,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DatabaseCrossReferenceImpl.class, qnDatabaseCrossReference),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DatabaseCrossReferenceImpl.class, qnDatabaseCrossReference));
		QName qnDataElementConceptRelationship = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DataElementConceptRelationshipImpl");
		call.registerTypeMapping(DataElementConceptRelationshipImpl.class, qnDataElementConceptRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DataElementConceptRelationshipImpl.class, qnDataElementConceptRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DataElementConceptRelationshipImpl.class, qnDataElementConceptRelationship));
		QName qnDataElementDerivation = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DataElementDerivationImpl");
		call.registerTypeMapping(DataElementDerivationImpl.class, qnDataElementDerivation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DataElementDerivationImpl.class, qnDataElementDerivation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DataElementDerivationImpl.class, qnDataElementDerivation));
		QName qnDataElementRelationship = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DataElementRelationshipImpl");
		call.registerTypeMapping(DataElementRelationshipImpl.class, qnDataElementRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DataElementRelationshipImpl.class, qnDataElementRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DataElementRelationshipImpl.class, qnDataElementRelationship));
		QName qnDefinitionClassSchemeItem = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DefinitionClassSchemeItemImpl");
		call.registerTypeMapping(DefinitionClassSchemeItemImpl.class, qnDefinitionClassSchemeItem,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DefinitionClassSchemeItemImpl.class, qnDefinitionClassSchemeItem),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DefinitionClassSchemeItemImpl.class, qnDefinitionClassSchemeItem));
		QName qnDefinition = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DefinitionImpl");
		call.registerTypeMapping(DefinitionImpl.class, qnDefinition,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DefinitionImpl.class, qnDefinition),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DefinitionImpl.class, qnDefinition));
		QName qnDerivationType = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DerivationTypeImpl");
		call.registerTypeMapping(DerivationTypeImpl.class, qnDerivationType,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DerivationTypeImpl.class, qnDerivationType),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DerivationTypeImpl.class, qnDerivationType));
		QName qnDerivedDataElement = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DerivedDataElementImpl");
		call.registerTypeMapping(DerivedDataElementImpl.class, qnDerivedDataElement,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DerivedDataElementImpl.class, qnDerivedDataElement),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DerivedDataElementImpl.class, qnDerivedDataElement));
		QName qnDesignationClassSchemeItem = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DesignationClassSchemeItemImpl");
		call.registerTypeMapping(DesignationClassSchemeItemImpl.class, qnDesignationClassSchemeItem,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DesignationClassSchemeItemImpl.class, qnDesignationClassSchemeItem),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DesignationClassSchemeItemImpl.class, qnDesignationClassSchemeItem));
		QName qnDesignation = new QName("urn:ws.domain.cadsr.nci.nih.gov", "DesignationImpl");
		call.registerTypeMapping(DesignationImpl.class, qnDesignation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DesignationImpl.class, qnDesignation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DesignationImpl.class, qnDesignation));
		QName qnDiseaseOntology = new QName("urn:ws.domain.cabio.nci.nih.gov", "DiseaseOntologyImpl");
		call.registerTypeMapping(DiseaseOntologyImpl.class, qnDiseaseOntology,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DiseaseOntologyImpl.class, qnDiseaseOntology),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DiseaseOntologyImpl.class, qnDiseaseOntology));
		QName qnDiseaseOntologyRelationship = new QName("urn:ws.domain.cabio.nci.nih.gov", "DiseaseOntologyRelationshipImpl");
		call.registerTypeMapping(DiseaseOntologyRelationshipImpl.class, qnDiseaseOntologyRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DiseaseOntologyRelationshipImpl.class, qnDiseaseOntologyRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DiseaseOntologyRelationshipImpl.class, qnDiseaseOntologyRelationship));
		QName qnEndpointCode = new QName("urn:ws.domain.camod.nci.nih.gov", "EndpointCodeImpl");
		call.registerTypeMapping(EndpointCodeImpl.class, qnEndpointCode,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(EndpointCodeImpl.class, qnEndpointCode),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(EndpointCodeImpl.class, qnEndpointCode));
		QName qnEngineeredGene = new QName("urn:ws.domain.camod.nci.nih.gov", "EngineeredGeneImpl");
		call.registerTypeMapping(EngineeredGeneImpl.class, qnEngineeredGene,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(EngineeredGeneImpl.class, qnEngineeredGene),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(EngineeredGeneImpl.class, qnEngineeredGene));
		QName qnTransgene = new QName("urn:ws.domain.camod.nci.nih.gov", "TransgeneImpl");
		call.registerTypeMapping(TransgeneImpl.class, qnTransgene,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TransgeneImpl.class, qnTransgene),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TransgeneImpl.class, qnTransgene));
		QName qnEnvironmentalFactor = new QName("urn:ws.domain.camod.nci.nih.gov", "EnvironmentalFactorImpl");
		call.registerTypeMapping(EnvironmentalFactorImpl.class, qnEnvironmentalFactor,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(EnvironmentalFactorImpl.class, qnEnvironmentalFactor),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(EnvironmentalFactorImpl.class, qnEnvironmentalFactor));
		QName qnExpressionFeature = new QName("urn:ws.domain.camod.nci.nih.gov", "ExpressionFeatureImpl");
		call.registerTypeMapping(ExpressionFeatureImpl.class, qnExpressionFeature,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ExpressionFeatureImpl.class, qnExpressionFeature),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ExpressionFeatureImpl.class, qnExpressionFeature));
		QName qnGeneAlias = new QName("urn:ws.domain.cabio.nci.nih.gov", "GeneAliasImpl");
		call.registerTypeMapping(GeneAliasImpl.class, qnGeneAlias,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneAliasImpl.class, qnGeneAlias),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneAliasImpl.class, qnGeneAlias));
		QName qnGeneDelivery = new QName("urn:ws.domain.camod.nci.nih.gov", "GeneDeliveryImpl");
		call.registerTypeMapping(GeneDeliveryImpl.class, qnGeneDelivery,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneDeliveryImpl.class, qnGeneDelivery),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneDeliveryImpl.class, qnGeneDelivery));
		QName qnGeneFunction = new QName("urn:ws.domain.camod.nci.nih.gov", "GeneFunctionImpl");
		call.registerTypeMapping(GeneFunctionImpl.class, qnGeneFunction,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneFunctionImpl.class, qnGeneFunction),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneFunctionImpl.class, qnGeneFunction));
		QName qnGene = new QName("urn:ws.domain.cabio.nci.nih.gov", "GeneImpl");
		call.registerTypeMapping(GeneImpl.class, qnGene,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneImpl.class, qnGene),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneImpl.class, qnGene));
		QName qnGeneOntology = new QName("urn:ws.domain.cabio.nci.nih.gov", "GeneOntologyImpl");
		call.registerTypeMapping(GeneOntologyImpl.class, qnGeneOntology,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneOntologyImpl.class, qnGeneOntology),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneOntologyImpl.class, qnGeneOntology));
		QName qnGeneOntologyRelationship = new QName("urn:ws.domain.cabio.nci.nih.gov", "GeneOntologyRelationshipImpl");
		call.registerTypeMapping(GeneOntologyRelationshipImpl.class, qnGeneOntologyRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneOntologyRelationshipImpl.class, qnGeneOntologyRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneOntologyRelationshipImpl.class, qnGeneOntologyRelationship));
		QName qnGeneRelativeLocation = new QName("urn:ws.domain.cabio.nci.nih.gov", "GeneRelativeLocationImpl");
		call.registerTypeMapping(GeneRelativeLocationImpl.class, qnGeneRelativeLocation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneRelativeLocationImpl.class, qnGeneRelativeLocation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneRelativeLocationImpl.class, qnGeneRelativeLocation));
		QName qnGenericArray = new QName("urn:ws.domain.cabio.nci.nih.gov", "GenericArrayImpl");
		call.registerTypeMapping(GenericArrayImpl.class, qnGenericArray,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GenericArrayImpl.class, qnGenericArray),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GenericArrayImpl.class, qnGenericArray));
		QName qnGenericReporter = new QName("urn:ws.domain.cabio.nci.nih.gov", "GenericReporterImpl");
		call.registerTypeMapping(GenericReporterImpl.class, qnGenericReporter,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GenericReporterImpl.class, qnGenericReporter),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GenericReporterImpl.class, qnGenericReporter));
		QName qnGeneticAlteration = new QName("urn:ws.domain.camod.nci.nih.gov", "GeneticAlterationImpl");
		call.registerTypeMapping(GeneticAlterationImpl.class, qnGeneticAlteration,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GeneticAlterationImpl.class, qnGeneticAlteration),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GeneticAlterationImpl.class, qnGeneticAlteration));
		QName qnGenomicSegment = new QName("urn:ws.domain.camod.nci.nih.gov", "GenomicSegmentImpl");
		call.registerTypeMapping(GenomicSegmentImpl.class, qnGenomicSegment,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GenomicSegmentImpl.class, qnGenomicSegment),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GenomicSegmentImpl.class, qnGenomicSegment));
		QName qnGenotypeSummary = new QName("urn:ws.domain.camod.nci.nih.gov", "GenotypeSummaryImpl");
		call.registerTypeMapping(GenotypeSummaryImpl.class, qnGenotypeSummary,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(GenotypeSummaryImpl.class, qnGenotypeSummary),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(GenotypeSummaryImpl.class, qnGenotypeSummary));
		QName qnHistopathology = new QName("urn:ws.domain.cabio.nci.nih.gov", "HistopathologyImpl");
		call.registerTypeMapping(HistopathologyImpl.class, qnHistopathology,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(HistopathologyImpl.class, qnHistopathology),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(HistopathologyImpl.class, qnHistopathology));
		QName qnHomologousAssociation = new QName("urn:ws.domain.cabio.nci.nih.gov", "HomologousAssociationImpl");
		call.registerTypeMapping(HomologousAssociationImpl.class, qnHomologousAssociation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(HomologousAssociationImpl.class, qnHomologousAssociation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(HomologousAssociationImpl.class, qnHomologousAssociation));
		QName qnImage = new QName("urn:ws.domain.camod.nci.nih.gov", "ImageImpl");
		call.registerTypeMapping(ImageImpl.class, qnImage,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ImageImpl.class, qnImage),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ImageImpl.class, qnImage));
		QName qnInducedMutation = new QName("urn:ws.domain.camod.nci.nih.gov", "InducedMutationImpl");
		call.registerTypeMapping(InducedMutationImpl.class, qnInducedMutation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(InducedMutationImpl.class, qnInducedMutation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(InducedMutationImpl.class, qnInducedMutation));
		QName qnIntegrationType = new QName("urn:ws.domain.camod.nci.nih.gov", "IntegrationTypeImpl");
		call.registerTypeMapping(IntegrationTypeImpl.class, qnIntegrationType,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(IntegrationTypeImpl.class, qnIntegrationType),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(IntegrationTypeImpl.class, qnIntegrationType));
		QName qnInvivoResult = new QName("urn:ws.domain.camod.nci.nih.gov", "InvivoResultImpl");
		call.registerTypeMapping(InvivoResultImpl.class, qnInvivoResult,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(InvivoResultImpl.class, qnInvivoResult),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(InvivoResultImpl.class, qnInvivoResult));
		QName qnJaxInfo = new QName("urn:ws.domain.camod.nci.nih.gov", "JaxInfoImpl");
		call.registerTypeMapping(JaxInfoImpl.class, qnJaxInfo,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(JaxInfoImpl.class, qnJaxInfo),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(JaxInfoImpl.class, qnJaxInfo));
		QName qnLibrary = new QName("urn:ws.domain.cabio.nci.nih.gov", "LibraryImpl");
		call.registerTypeMapping(LibraryImpl.class, qnLibrary,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(LibraryImpl.class, qnLibrary),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(LibraryImpl.class, qnLibrary));
		QName qnLocation = new QName("urn:ws.domain.cabio.nci.nih.gov", "LocationImpl");
		call.registerTypeMapping(LocationImpl.class, qnLocation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(LocationImpl.class, qnLocation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(LocationImpl.class, qnLocation));
		QName qnPhysicalLocation = new QName("urn:ws.domain.cabio.nci.nih.gov", "PhysicalLocationImpl");
		call.registerTypeMapping(PhysicalLocationImpl.class, qnPhysicalLocation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PhysicalLocationImpl.class, qnPhysicalLocation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PhysicalLocationImpl.class, qnPhysicalLocation));
		QName qnMicroArrayData = new QName("urn:ws.domain.camod.nci.nih.gov", "MicroArrayDataImpl");
		call.registerTypeMapping(MicroArrayDataImpl.class, qnMicroArrayData,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(MicroArrayDataImpl.class, qnMicroArrayData),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(MicroArrayDataImpl.class, qnMicroArrayData));
		QName qnModificationType = new QName("urn:ws.domain.camod.nci.nih.gov", "ModificationTypeImpl");
		call.registerTypeMapping(ModificationTypeImpl.class, qnModificationType,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ModificationTypeImpl.class, qnModificationType),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ModificationTypeImpl.class, qnModificationType));
		QName qnNomenclature = new QName("urn:ws.domain.camod.nci.nih.gov", "NomenclatureImpl");
		call.registerTypeMapping(NomenclatureImpl.class, qnNomenclature,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(NomenclatureImpl.class, qnNomenclature),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(NomenclatureImpl.class, qnNomenclature));
		QName qnNucleicAcidSequence = new QName("urn:ws.domain.cabio.nci.nih.gov", "NucleicAcidSequenceImpl");
		call.registerTypeMapping(NucleicAcidSequenceImpl.class, qnNucleicAcidSequence,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(NucleicAcidSequenceImpl.class, qnNucleicAcidSequence),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(NucleicAcidSequenceImpl.class, qnNucleicAcidSequence));

		QName qnOrganOntology = new QName("urn:ws.domain.cabio.nci.nih.gov", "OrganOntologyImpl");
		call.registerTypeMapping(OrganOntologyImpl.class, qnOrganOntology,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(OrganOntologyImpl.class, qnOrganOntology),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(OrganOntologyImpl.class, qnOrganOntology));
		QName qnOrganOntologyRelationship = new QName("urn:ws.domain.cabio.nci.nih.gov", "OrganOntologyRelationshipImpl");
		call.registerTypeMapping(OrganOntologyRelationshipImpl.class, qnOrganOntologyRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(OrganOntologyRelationshipImpl.class, qnOrganOntologyRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(OrganOntologyRelationshipImpl.class, qnOrganOntologyRelationship));
		QName qnParty = new QName("urn:ws.domain.camod.nci.nih.gov", "PartyImpl");
		call.registerTypeMapping(PartyImpl.class, qnParty,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PartyImpl.class, qnParty),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PartyImpl.class, qnParty));
		QName qnPartyRole = new QName("urn:ws.domain.camod.nci.nih.gov", "PartyRoleImpl");
		call.registerTypeMapping(PartyRoleImpl.class, qnPartyRole,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PartyRoleImpl.class, qnPartyRole),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PartyRoleImpl.class, qnPartyRole));
		QName qnPathway = new QName("urn:ws.domain.cabio.nci.nih.gov", "PathwayImpl");
		call.registerTypeMapping(PathwayImpl.class, qnPathway,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PathwayImpl.class, qnPathway),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PathwayImpl.class, qnPathway));
		QName qnPermissibleValue = new QName("urn:ws.domain.cadsr.nci.nih.gov", "PermissibleValueImpl");
		call.registerTypeMapping(PermissibleValueImpl.class, qnPermissibleValue,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PermissibleValueImpl.class, qnPermissibleValue),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PermissibleValueImpl.class, qnPermissibleValue));
		QName qnPhenotype = new QName("urn:ws.domain.camod.nci.nih.gov", "PhenotypeImpl");
		call.registerTypeMapping(PhenotypeImpl.class, qnPhenotype,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PhenotypeImpl.class, qnPhenotype),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PhenotypeImpl.class, qnPhenotype));
		QName qnPopulationFrequency = new QName("urn:ws.domain.cabio.nci.nih.gov", "PopulationFrequencyImpl");
		call.registerTypeMapping(PopulationFrequencyImpl.class, qnPopulationFrequency,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PopulationFrequencyImpl.class, qnPopulationFrequency),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PopulationFrequencyImpl.class, qnPopulationFrequency));
		QName qnPromoter = new QName("urn:ws.domain.camod.nci.nih.gov", "PromoterImpl");
		call.registerTypeMapping(PromoterImpl.class, qnPromoter,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PromoterImpl.class, qnPromoter),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PromoterImpl.class, qnPromoter));
		QName qnProteinAlias = new QName("urn:ws.domain.cabio.nci.nih.gov", "ProteinAliasImpl");
		call.registerTypeMapping(ProteinAliasImpl.class, qnProteinAlias,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ProteinAliasImpl.class, qnProteinAlias),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ProteinAliasImpl.class, qnProteinAlias));
		QName qnProtein = new QName("urn:ws.domain.cabio.nci.nih.gov", "ProteinImpl");
		call.registerTypeMapping(ProteinImpl.class, qnProtein,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ProteinImpl.class, qnProtein),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ProteinImpl.class, qnProtein));
		QName qnProteinSequence = new QName("urn:ws.domain.cabio.nci.nih.gov", "ProteinSequenceImpl");
		call.registerTypeMapping(ProteinSequenceImpl.class, qnProteinSequence,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ProteinSequenceImpl.class, qnProteinSequence),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ProteinSequenceImpl.class, qnProteinSequence));
		QName qnProtocolAssociation = new QName("urn:ws.domain.cabio.nci.nih.gov", "ProtocolAssociationImpl");
		call.registerTypeMapping(ProtocolAssociationImpl.class, qnProtocolAssociation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ProtocolAssociationImpl.class, qnProtocolAssociation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ProtocolAssociationImpl.class, qnProtocolAssociation));
		QName qnProvenance = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "ProvenanceImpl");
		call.registerTypeMapping(ProvenanceImpl.class, qnProvenance,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ProvenanceImpl.class, qnProvenance),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ProvenanceImpl.class, qnProvenance));
		QName qnPublication = new QName("urn:ws.domain.camod.nci.nih.gov", "PublicationImpl");
		call.registerTypeMapping(PublicationImpl.class, qnPublication,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PublicationImpl.class, qnPublication),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PublicationImpl.class, qnPublication));
		QName qnPublicationStatus = new QName("urn:ws.domain.camod.nci.nih.gov", "PublicationStatusImpl");
		call.registerTypeMapping(PublicationStatusImpl.class, qnPublicationStatus,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PublicationStatusImpl.class, qnPublicationStatus),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PublicationStatusImpl.class, qnPublicationStatus));
		QName qnReferenceDocument = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ReferenceDocumentImpl");
		call.registerTypeMapping(ReferenceDocumentImpl.class, qnReferenceDocument,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ReferenceDocumentImpl.class, qnReferenceDocument),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ReferenceDocumentImpl.class, qnReferenceDocument));
		QName qnRegulatoryElement = new QName("urn:ws.domain.camod.nci.nih.gov", "RegulatoryElementImpl");
		call.registerTypeMapping(RegulatoryElementImpl.class, qnRegulatoryElement,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(RegulatoryElementImpl.class, qnRegulatoryElement),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(RegulatoryElementImpl.class, qnRegulatoryElement));
		QName qnRegulatoryElementType = new QName("urn:ws.domain.camod.nci.nih.gov", "RegulatoryElementTypeImpl");
		call.registerTypeMapping(RegulatoryElementTypeImpl.class, qnRegulatoryElementType,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(RegulatoryElementTypeImpl.class, qnRegulatoryElementType),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(RegulatoryElementTypeImpl.class, qnRegulatoryElementType));
		QName qnRepositoryInfo = new QName("urn:ws.domain.camod.nci.nih.gov", "RepositoryInfoImpl");
		call.registerTypeMapping(RepositoryInfoImpl.class, qnRepositoryInfo,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(RepositoryInfoImpl.class, qnRepositoryInfo),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(RepositoryInfoImpl.class, qnRepositoryInfo));
		QName qnRole = new QName("urn:ws.domain.camod.nci.nih.gov", "RoleImpl");
		call.registerTypeMapping(RoleImpl.class, qnRole,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(RoleImpl.class, qnRole),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(RoleImpl.class, qnRole));
		QName qnScreeningResult = new QName("urn:ws.domain.camod.nci.nih.gov", "ScreeningResultImpl");
		call.registerTypeMapping(ScreeningResultImpl.class, qnScreeningResult,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ScreeningResultImpl.class, qnScreeningResult),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ScreeningResultImpl.class, qnScreeningResult));
		QName qnSegmentType = new QName("urn:ws.domain.camod.nci.nih.gov", "SegmentTypeImpl");
		call.registerTypeMapping(SegmentTypeImpl.class, qnSegmentType,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(SegmentTypeImpl.class, qnSegmentType),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(SegmentTypeImpl.class, qnSegmentType));
		QName qnSexDistribution = new QName("urn:ws.domain.camod.nci.nih.gov", "SexDistributionImpl");
		call.registerTypeMapping(SexDistributionImpl.class, qnSexDistribution,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(SexDistributionImpl.class, qnSexDistribution),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(SexDistributionImpl.class, qnSexDistribution));
		QName qnSNP = new QName("urn:ws.domain.cabio.nci.nih.gov", "SNPImpl");
		call.registerTypeMapping(SNPImpl.class, qnSNP,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(SNPImpl.class, qnSNP),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(SNPImpl.class, qnSNP));
		QName qnSource = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "SourceImpl");
		call.registerTypeMapping(SourceImpl.class, qnSource,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(SourceImpl.class, qnSource),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(SourceImpl.class, qnSource));
		QName qnPublicationSource = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "PublicationSourceImpl");
		call.registerTypeMapping(PublicationSourceImpl.class, qnPublicationSource,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(PublicationSourceImpl.class, qnPublicationSource),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(PublicationSourceImpl.class, qnPublicationSource));
		QName qnResearchInstitutionSource = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "ResearchInstitutionSourceImpl");
		call.registerTypeMapping(ResearchInstitutionSourceImpl.class, qnResearchInstitutionSource,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ResearchInstitutionSourceImpl.class, qnResearchInstitutionSource),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ResearchInstitutionSourceImpl.class, qnResearchInstitutionSource));
		QName qnInternetSource = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "InternetSourceImpl");
		call.registerTypeMapping(InternetSourceImpl.class, qnInternetSource,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(InternetSourceImpl.class, qnInternetSource),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(InternetSourceImpl.class, qnInternetSource));
		QName qnSourceReference = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "SourceReferenceImpl");
		call.registerTypeMapping(SourceReferenceImpl.class, qnSourceReference,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(SourceReferenceImpl.class, qnSourceReference),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(SourceReferenceImpl.class, qnSourceReference));
		QName qnWebServicesSourceReference = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "WebServicesSourceReferenceImpl");
		call.registerTypeMapping(WebServicesSourceReferenceImpl.class, qnWebServicesSourceReference,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(WebServicesSourceReferenceImpl.class, qnWebServicesSourceReference),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(WebServicesSourceReferenceImpl.class, qnWebServicesSourceReference));
		QName qnURLSourceReference = new QName("urn:ws.domain.provenance.common.nci.nih.gov", "URLSourceReferenceImpl");
		call.registerTypeMapping(URLSourceReferenceImpl.class, qnURLSourceReference,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(URLSourceReferenceImpl.class, qnURLSourceReference),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(URLSourceReferenceImpl.class, qnURLSourceReference));
		QName qnTargetedModification = new QName("urn:ws.domain.camod.nci.nih.gov", "TargetedModificationImpl");
		call.registerTypeMapping(TargetedModificationImpl.class, qnTargetedModification,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TargetedModificationImpl.class, qnTargetedModification),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TargetedModificationImpl.class, qnTargetedModification));
		QName qnTarget = new QName("urn:ws.domain.cabio.nci.nih.gov", "TargetImpl");
		call.registerTypeMapping(TargetImpl.class, qnTarget,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TargetImpl.class, qnTarget),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TargetImpl.class, qnTarget));
		QName qnTaxon = new QName("urn:ws.domain.cabio.nci.nih.gov", "TaxonImpl");
		call.registerTypeMapping(TaxonImpl.class, qnTaxon,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TaxonImpl.class, qnTaxon),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TaxonImpl.class, qnTaxon));
		QName qnTherapy = new QName("urn:ws.domain.camod.nci.nih.gov", "TherapyImpl");
		call.registerTypeMapping(TherapyImpl.class, qnTherapy,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TherapyImpl.class, qnTherapy),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TherapyImpl.class, qnTherapy));
		QName qnTissue = new QName("urn:ws.domain.cabio.nci.nih.gov", "TissueImpl");
		call.registerTypeMapping(TissueImpl.class, qnTissue,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TissueImpl.class, qnTissue),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TissueImpl.class, qnTissue));
		QName qnTreatmentSchedule = new QName("urn:ws.domain.camod.nci.nih.gov", "TreatmentScheduleImpl");
		call.registerTypeMapping(TreatmentScheduleImpl.class, qnTreatmentSchedule,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TreatmentScheduleImpl.class, qnTreatmentSchedule),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TreatmentScheduleImpl.class, qnTreatmentSchedule));
		QName qnTumorCode = new QName("urn:ws.domain.camod.nci.nih.gov", "TumorCodeImpl");
		call.registerTypeMapping(TumorCodeImpl.class, qnTumorCode,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(TumorCodeImpl.class, qnTumorCode),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(TumorCodeImpl.class, qnTumorCode));
		QName qnValueDomainPermissibleValue = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ValueDomainPermissibleValueImpl");
		call.registerTypeMapping(ValueDomainPermissibleValueImpl.class, qnValueDomainPermissibleValue,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ValueDomainPermissibleValueImpl.class, qnValueDomainPermissibleValue),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ValueDomainPermissibleValueImpl.class, qnValueDomainPermissibleValue));
		QName qnValueDomainRelationship = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ValueDomainRelationshipImpl");
		call.registerTypeMapping(ValueDomainRelationshipImpl.class, qnValueDomainRelationship,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ValueDomainRelationshipImpl.class, qnValueDomainRelationship),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ValueDomainRelationshipImpl.class, qnValueDomainRelationship));
		QName qnValueMeaning = new QName("urn:ws.domain.cadsr.nci.nih.gov", "ValueMeaningImpl");
		call.registerTypeMapping(ValueMeaningImpl.class, qnValueMeaning,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(ValueMeaningImpl.class, qnValueMeaning),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(ValueMeaningImpl.class, qnValueMeaning));
		QName qnVocabulary = new QName("urn:ws.domain.cabio.nci.nih.gov", "VocabularyImpl");
		call.registerTypeMapping(VocabularyImpl.class, qnVocabulary,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(VocabularyImpl.class, qnVocabulary),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(VocabularyImpl.class, qnVocabulary));

		QName qnCytogeneticLocation = new QName("urn:ws.domain.cabio.nci.nih.gov", "CytogeneticLocationImpl");
		call.registerTypeMapping(CytogeneticLocationImpl.class, qnCytogeneticLocation,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(CytogeneticLocationImpl.class, qnCytogeneticLocation),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(CytogeneticLocationImpl.class, qnCytogeneticLocation));
		QName qnEnumeratedValueDomain = new QName("urn:ws.domain.cabio.nci.nih.gov", "EnumeratedValueDomainImpl");
		call.registerTypeMapping(EnumeratedValueDomainImpl.class, qnEnumeratedValueDomain,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(EnumeratedValueDomainImpl.class, qnEnumeratedValueDomain),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(EnumeratedValueDomainImpl.class, qnEnumeratedValueDomain));
		QName qnDataElement = new QName("urn:ws.domain.cabio.nci.nih.gov", "DataElementImpl");
		call.registerTypeMapping(DataElementImpl.class, qnDataElement,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DataElementImpl.class, qnDataElement),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DataElementImpl.class, qnDataElement));
		QName qnDataElementConcept = new QName("urn:ws.domain.cabio.nci.nih.gov", "DataElementConceptImpl");
		call.registerTypeMapping(DataElementConceptImpl.class, qnDataElementConcept,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(DataElementConceptImpl.class, qnDataElementConcept),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(DataElementConceptImpl.class, qnDataElementConcept));
		QName qnNonenumeratedValueDomain = new QName("urn:ws.domain.cabio.nci.nih.gov", "NonenumeratedValueDomainImpl");
		call.registerTypeMapping(NonenumeratedValueDomainImpl.class, qnNonenumeratedValueDomain,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(NonenumeratedValueDomainImpl.class, qnNonenumeratedValueDomain),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(NonenumeratedValueDomainImpl.class, qnNonenumeratedValueDomain));
		QName qnYeastModel = new QName("urn:ws.domain.camod.nci.nih.gov", "YeastModelImpl");
		call.registerTypeMapping(YeastModelImpl.class, qnYeastModel,
				new org.apache.axis.encoding.ser.BeanSerializerFactory(YeastModelImpl.class, qnYeastModel),
				new org.apache.axis.encoding.ser.BeanDeserializerFactory(YeastModelImpl.class, qnYeastModel));

		/****************************************************************************************************************/

		String url = "http://@WEB_SERVER_NAME@:@WEB_SERVER_PORT@/@PROJECT_NAME@/ws/@WEBSERVICENAME@";

		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("caCoreWebService", "queryObject"));
		call.addParameter("arg1",
				org.apache.axis.encoding.XMLType.XSD_STRING,
				ParameterMode.IN);
		call.addParameter("arg2", qnGene, ParameterMode.IN);
		call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_ARRAY);
		gov.nih.nci.cabio.domain.ws.GeneImpl gene = new gov.nih.nci.cabio.domain.ws.GeneImpl();
		gene.setSymbol("NAT2");
		Object[] resultList = (Object[])call.invoke(new Object[] { "gov.nih.nci.cabio.domain.ws.ChromosomeImpl", gene });
		System.out.println("Total Objects Found: " + resultList.length);
		if (resultList.length > 0) {
			for(int resultIndex = 0; resultIndex < resultList.length; resultIndex++) {
				ChromosomeImpl ch = (ChromosomeImpl)resultList[resultIndex];
				System.out.println("Chromosome Number " + ch.getNumber());
			}
		}
	}

}
