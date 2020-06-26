Application Insights Agent Extractor for Azure Spring Cloud
===========================================================

## The problem
The application insights agent built into Azure Spring Cloud is not the latest and greatest. To try the latest preview agent,
you need to include a different application insights agent jar file in the startup parameters for your application.
You can enable persistent storage and use the insights jar from there, but there is no direct access to the persistent storage
area, so you can't put the agent file there manually.

## The solution
This simple Spring Boot app that writes the application insights jar and json settings file to any given location

## Usage

1. Create an ASC application in your ASC instance
2. Enable persistent storage
3. Deploy the extractor jar with extractor runtime args:
        
         -Dextraction.path=[PATH_TO_WRITE_INSIGHTS_JAR_AND_SETTINGS_FILE]\
         -Dinstrumentation.key=[APPINSIGHTS_INSTRUMENTATION_KEY]\
         -Drole.name=[OPTIONAL_ROLE_NAME_FOR_APPINSIGHTS]
         
 4. Deploy your real Spring Boot application in place of the extractor with agent runtime args.    