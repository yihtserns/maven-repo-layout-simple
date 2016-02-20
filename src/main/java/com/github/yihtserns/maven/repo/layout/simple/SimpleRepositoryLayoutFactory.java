/*
 * Copyright 2015 yihtserns.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yihtserns.maven.repo.layout.simple;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import javax.inject.Named;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.metadata.Metadata;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.layout.RepositoryLayout;
import org.eclipse.aether.spi.connector.layout.RepositoryLayoutFactory;
import org.eclipse.aether.transfer.NoRepositoryLayoutException;

/**
 *
 * @author yihtserns
 */
@Named("simplerepo")
public class SimpleRepositoryLayoutFactory implements RepositoryLayoutFactory {

    @Override
    public RepositoryLayout newInstance(RepositorySystemSession session, RemoteRepository repository) throws NoRepositoryLayoutException {
        if (!"simple".equals(repository.getContentType())) {
            throw new NoRepositoryLayoutException(repository);
        }
        return SimpleRepositoryLayout.INSTANCE;
    }

    @Override
    public float getPriority() {
        return -1;
    }

    private enum SimpleRepositoryLayout implements RepositoryLayout {

        INSTANCE;

        @Override
        public URI getLocation(Artifact artifact, boolean upload) {
            throwIfUpload(upload);

            String path = artifact.getGroupId()
                    + "/" + artifact.getBaseVersion()
                    + "/" + artifact.getArtifactId()
                    + "." + artifact.getExtension();

            return toUri(path);
        }

        public URI toUri(String path) throws IllegalStateException {
            try {
                return new URI(null, null, path, null);
            } catch (URISyntaxException ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public URI getLocation(Metadata metadata, boolean upload) {
            throwIfUpload(upload);

            return toUri("todo.xml");
        }

        @Override
        public List<Checksum> getChecksums(Artifact artifact, boolean upload, URI location) {
            throwIfUpload(upload);

            return Collections.emptyList();
        }

        @Override
        public List<Checksum> getChecksums(Metadata metadata, boolean upload, URI location) {
            throwIfUpload(upload);

            return Collections.emptyList();
        }

        private void throwIfUpload(boolean isUpload) {
            if (isUpload) {
                throw new UnsupportedOperationException("Not implemented yet.");
            }
        }
    }
}
