package gov.nih.nci.cacoresdk.generator.common;

/**
 * Constants used in the SDK Generator Portal
 * 
 * @author <A HREF="MAILTO:dumitrud@mail.nih.gov">Daniel Dumitru </A>
 * @author <A HREF="MAILTO:patelsat@mail.nih.gov">Satish Patel </A>
 */
public abstract class SdkGeneratorConstants {

	// Generator specific constants
	public static final String SDK_GENERATOR_VERSION_PROPERTY = "sdk.generator.version";

	public static final String SDK_GENERATOR_ENGINE_PROPERTIES = "conf/sdk.generator.engine.properties";

	private SdkGeneratorConstants() {
		// prevents instantiation
	}
}
