package dk.gundmann.jenkins.cddbplugin;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import dk.gundmann.jenkins.cddbplugin.commands.Connector;
import dk.gundmann.jenkins.cddbplugin.commands.DriverClassLoader;
import dk.gundmann.jenkins.cddbplugin.commands.ResolveSqlScript;
import dk.gundmann.jenkins.cddbplugin.commands.TableNameResolver;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

/**
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link CDDB} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Tim Gundmann
 */
public class CDDB extends Builder {

    private final String jdbcPath;
	private final String connctionString;
	private final String versionTableName;
	private final String version;

    @DataBoundConstructor
    public CDDB(String jdbcPath, String connectionString, String versionTableName, String version) {
		this.jdbcPath = jdbcPath;
		this.connctionString = connectionString;
		this.versionTableName = versionTableName;
		this.version = version;
    }

    public String getJdbcPath() {
        return jdbcPath;
    }

	public String getConnectionString() {
		return connctionString;
	}

	public String getVersionTableName() {
		return versionTableName;
	}

	public String getVersion() {
		return version;
	}

	@Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
    	exeucteCommands(setUpCommands(), setUpParemters());
    	return true;
    }

    private void exeucteCommands(List<Command> commands, Parameters parameters) {
		for (Command command : commands) {
			Result result = command.execute(parameters);
			if (result.failed()) {
				throw new DBUpdateException(result);
			}
		}
	}

	private List<Command> setUpCommands() {
    	return Arrays.asList(new Command[] {
    			new TableNameResolver(),
    			new DriverClassLoader(),
    			new Connector(),
    			new ResolveSqlScript()
    	});
	}

	private Parameters setUpParemters() {
		return new Parameters(
				Parameter.aBuilder()
				.withKey(ResolveSqlScript.KEY_VERSION)
				.withValue(version)
				.build(),
			Parameter.aBuilder()
				.withKey(DriverClassLoader.KEY_JAR_FILE_NAME)
				.withValue(jdbcPath)
				.build(),
			Parameter.aBuilder()
				.withKey(Connector.KEY_CONNECTION_STRING)
				.withValue(connctionString)
				.build(),
			Parameter.aBuilder()
				.withKey(TableNameResolver.KEY_VERSION_TABLE_NAME)
				.withValue(versionTableName)
				.build());

	}

	@Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

	/**
     * Descriptor for {@link CDDB}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a name");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }

        @SuppressWarnings("rawtypes")
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Database update";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            save();
            return super.configure(req,formData);
        }

    }
}

