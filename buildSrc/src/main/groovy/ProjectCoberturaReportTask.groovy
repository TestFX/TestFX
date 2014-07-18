/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import net.sourceforge.cobertura.reporting.Main

/**
 * @author Andres Almiray
 */
class ProjectCoberturaReportTask extends DefaultTask {
    @TaskAction
    void report() {
        def args = []
        def coberturaDir = new File(project.buildDir, 'cobertura')
        args << '--datafile'
        args << "${coberturaDir.absolutePath}/cobertura.ser"
        args << '--format'
        args << 'html'
        args << '--destination'
        args << new File(project.buildDir, 'reports/cobertura')
        project.subprojects.each { prj ->
            if (prj.plugins.hasPlugin('java')) {
                args.addAll(prj.sourceSets.main.java.srcDirs)
            }

            File coberturaOutputDir = new File(prj.projectDir, 'build/cobertura')
            if (coberturaOutputDir.exists()) {
                coberturaOutputDir.eachFile {
                    args << "${it.absolutePath - project.projectDir.absolutePath}"
                }
            }
        }
        Main.main(args as String[])

        args[3] = 'xml'
        Main.main(args as String[])
    }
}