// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.launcher.repositories;

import com.google.common.collect.Sets;
import org.terasology.launcher.model.Build;
import org.terasology.launcher.model.GameRelease;
import org.terasology.launcher.model.Profile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RepositoryManager {
    private static final String JENKINS_BASE_URL = "http://jenkins.terasology.org/";

    private final Set<GameRelease> releases;

    public RepositoryManager() {
        ReleaseRepository terasologyNightly = new JenkinsRepositoryAdapter(JENKINS_BASE_URL, "Terasology", Build.NIGHTLY, Profile.ENGINE);
        ReleaseRepository terasologyStable = new JenkinsRepositoryAdapter(JENKINS_BASE_URL, "TerasologyStable", Build.STABLE, Profile.ENGINE);
        ReleaseRepository omegaNightly = new JenkinsRepositoryAdapter(JENKINS_BASE_URL, "DistroOmega", Build.NIGHTLY, Profile.OMEGA);
        ReleaseRepository omegaStable = new JenkinsRepositoryAdapter(JENKINS_BASE_URL, "DistroOmegaRelease", Build.STABLE, Profile.OMEGA);

        Set<ReleaseRepository> all = Sets.newHashSet(terasologyNightly, terasologyStable, omegaNightly, omegaStable);

        releases = fetchReleases(all);
    }

    private Set<GameRelease> fetchReleases(final Set<ReleaseRepository> repositories) {
        return repositories.stream()
                .map(ReleaseRepository::fetchReleases)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    public Set<GameRelease> getReleases() {
        return releases;
    }

}
