package com.team3841.SwerveLib.swervelib.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.first.math.util.Units;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.team3841.SwerveLib.swervelib.SwerveDrive;
import com.team3841.SwerveLib.swervelib.SwerveModule;
import com.team3841.SwerveLib.swervelib.imu.NavXSwerve;
import com.team3841.SwerveLib.swervelib.parser.json.ControllerPropertiesJson;
import com.team3841.SwerveLib.swervelib.parser.json.ModuleJson;
import com.team3841.SwerveLib.swervelib.parser.json.PIDFPropertiesJson;
import com.team3841.SwerveLib.swervelib.parser.json.PhysicalPropertiesJson;
import com.team3841.SwerveLib.swervelib.parser.json.SwerveDriveJson;

/**
 * Helper class used to parse the JSON directory with specified configuration options.
 */
public class SwerveParser
{

  /**
   * Module number mapped to the JSON name.
   */
  private static final HashMap<String, Integer> moduleConfigs = new HashMap<>();
  /**
   * Parsed swervedrive.json
   */
  public static        SwerveDriveJson          swerveDriveJson;
  /**
   * Parsed controllerproperties.json
   */
  public static        ControllerPropertiesJson controllerPropertiesJson;
  /**
   * Parsed modules/pidfproperties.json
   */
  public static        PIDFPropertiesJson       pidfPropertiesJson;
  /**
   * Parsed modules/physicalproperties.json
   */
  public static        PhysicalPropertiesJson   physicalPropertiesJson;
  /**
   * Array holding the module jsons given in {@link SwerveDriveJson}.
   */
  public static        ModuleJson[]             moduleJsons;

  /**
   * Construct a swerve parser. Will throw an error if there is a missing file.
   *
   * @param directory Directory with swerve configurations.
   * @throws IOException if a file doesn't exist.
   */
  public SwerveParser(File directory) throws IOException
  {
    checkDirectory(directory);
    swerveDriveJson =
        new ObjectMapper()
            .readValue(new File(directory, "swervedrive.json"), SwerveDriveJson.class);
    controllerPropertiesJson =
        new ObjectMapper()
            .readValue(
                new File(directory, "controllerproperties.json"), ControllerPropertiesJson.class);
    pidfPropertiesJson =
        new ObjectMapper()
            .readValue(
                new File(directory, "modules/pidfproperties.json"), PIDFPropertiesJson.class);
    physicalPropertiesJson =
        new ObjectMapper()
            .readValue(
                new File(directory, "modules/physicalproperties.json"),
                PhysicalPropertiesJson.class);
    moduleJsons = new ModuleJson[swerveDriveJson.modules.length];
    for (int i = 0; i < moduleJsons.length; i++)
    {
      moduleConfigs.put(swerveDriveJson.modules[i], i);
      File moduleFile = new File(directory, "modules/" + swerveDriveJson.modules[i]);
      assert moduleFile.exists();
      moduleJsons[i] = new ObjectMapper().readValue(moduleFile, ModuleJson.class);
    }
  }

  /**
   * Get the swerve module by the json name.
   *
   * @param name               JSON name.
   * @param driveConfiguration {@link SwerveDriveConfiguration} to pull from.
   * @return {@link SwerveModuleConfiguration} based on the file.
   */
  public static SwerveModule getModuleConfigurationByName(
      String name, SwerveDriveConfiguration driveConfiguration)
  {
    return driveConfiguration.modules[moduleConfigs.get(name + ".json")];
  }

  /**
   * Open JSON file.
   *
   * @param file JSON File to open.
   * @return JsonNode of file.
   */
  private JsonNode openJson(File file)
  {
    try
    {
      return new ObjectMapper().readTree(file);
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Check directory structure.
   *
   * @param directory JSON Configuration Directory
   */
  private void checkDirectory(File directory)
  {
    assert new File(directory, "swervedrive.json").exists();
    assert new File(directory, "controllerproperties.json").exists();
    assert new File(directory, "modules").exists() && new File(directory, "modules").isDirectory();
    assert new File(directory, "modules/pidfproperties.json").exists();
    assert new File(directory, "modules/physicalproperties.json").exists();
  }

  /**
   * Create {@link SwerveDrive} from JSON configuration directory.
   *
   * @return {@link SwerveDrive} instance.
   */
  public SwerveDrive createSwerveDrive()
  {
    double maxSpeedMPS = Units.feetToMeters(swerveDriveJson.maxSpeed);
    SwerveModuleConfiguration[] moduleConfigurations =
        new SwerveModuleConfiguration[moduleJsons.length];
    for (int i = 0; i < moduleConfigurations.length; i++)
    {
      ModuleJson module = moduleJsons[i];
      moduleConfigurations[i] =
          module.createModuleConfiguration(
              pidfPropertiesJson.angle,
              pidfPropertiesJson.drive,
              maxSpeedMPS,
              physicalPropertiesJson.createPhysicalProperties(swerveDriveJson.optimalVoltage),
              swerveDriveJson.modules[i]);
    }
    SwerveDriveConfiguration swerveDriveConfiguration =
        new SwerveDriveConfiguration(
            moduleConfigurations,
            (NavXSwerve) swerveDriveJson.imu.createIMU(),
            maxSpeedMPS,
            swerveDriveJson.invertedIMU);

    return new SwerveDrive(
        swerveDriveConfiguration,
        controllerPropertiesJson.createControllerConfiguration(swerveDriveConfiguration));
  }
}
