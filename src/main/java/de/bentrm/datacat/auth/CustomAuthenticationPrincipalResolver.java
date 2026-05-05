package de.bentrm.datacat.auth;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Custom Expression Handler der 'principal' in @PreAuthorize korrekt auflöst.
 * Für Legacy-Tokens: principal ist der Username (String)
 * Für Keycloak-Tokens: principal ist der Username aus JWT Claims (extrahiert aus Jwt-Objekt)
 */
public class CustomAuthenticationPrincipalResolver extends DefaultMethodSecurityExpressionHandler {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix(getDefaultRolePrefix());
        
        return root;
    }

    /**
     * Custom Root-Objekt, das 'principal' korrekt als Username-String auflöst
     */
    private static class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot 
            implements MethodSecurityExpressionOperations {

        private Object filterObject;
        private Object returnObject;
        private Object target;

        CustomMethodSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }

        /**
         * Überschreibt getPrincipal() um sowohl Legacy als auch Keycloak Tokens zu unterstützen.
         * Gibt immer den Username als String zurück.
         */
        @Override
        public Object getPrincipal() {
            Object principal = super.getPrincipal();
            
            if (principal instanceof Jwt jwt) {
                // Keycloak JWT - extrahiere Username aus Claims
                String username = jwt.getClaimAsString("preferred_username");
                return username != null ? username : jwt.getClaimAsString("sub");
            }
            
            // Legacy JWT - Principal ist bereits der Username
            return principal;
        }

        @Override
        public void setFilterObject(Object filterObject) {
            this.filterObject = filterObject;
        }

        @Override
        public Object getFilterObject() {
            return this.filterObject;
        }

        @Override
        public void setReturnObject(Object returnObject) {
            this.returnObject = returnObject;
        }

        @Override
        public Object getReturnObject() {
            return this.returnObject;
        }

        @Override
        public Object getThis() {
            return this.target;
        }
    }
}
