/*
 * SonarQube Scanner for Jenkins
 * Copyright (C) 2007-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package hudson.plugins.sonar.configurationslicing;

import hudson.maven.MavenModuleSet;
import hudson.plugins.sonar.SonarPublisher;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author drautureau
 */
public class SonarPublisherJdkSlicerTest {

  @Rule
  public JenkinsRule j = new JenkinsRule();

  @Test
  public void availableMavenProjectsWithSonarPublisher() throws IOException {
    final MavenModuleSet project = j.jenkins.createProject(MavenModuleSet.class, "random-name");
    assertThat(new SonarPublisherJdkSlicer().getWorkDomain().size()).isZero();
    project.getPublishersList().add(new SonarPublisher("MySonar", null, null, null, null, null, null, null, null, null, false));
    assertThat(new SonarPublisherJdkSlicer().getWorkDomain().size()).isEqualTo(1);
  }

  @Test
  public void changeJobAdditionalProperties() throws Exception {
    final MavenModuleSet project = j.jenkins.createProject(MavenModuleSet.class, "random-name");
    final SonarPublisher mySonar = new SonarPublisher("MySonar", null, null, null, null, null, null, "1.7", null, null, false);
    project.getPublishersList().add(mySonar);

    final SonarPublisherJdkSlicer.SonarPublisherJdkSlicerSpec spec = new SonarPublisherJdkSlicer.SonarPublisherJdkSlicerSpec();
    final List<String> values = spec.getValues(project);
    assertThat(values.get(0)).isEqualTo("1.7");
    final List<String> newValues = new ArrayList<>();
    newValues.add("1.7");
    spec.setValues(project, newValues);

    assertThat(mySonar.getJdkName()).isEqualTo("1.7");
  }

}
