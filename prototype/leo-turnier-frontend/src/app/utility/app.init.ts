import {KeycloakService} from 'keycloak-angular';

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8443',
        realm: 'leoturnier',
        clientId: 'angular-leoturnier-client'
      },
      initOptions: {
        checkLoginIframe: false,
        checkLoginIframeInterval: 25,
        onLoad: 'check-sso'
      },
      loadUserProfileAtStartUp: true
    });
}
