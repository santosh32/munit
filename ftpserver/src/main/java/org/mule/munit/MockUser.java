package org.mule.munit;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.TransferRateRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/10/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class MockUser implements User {
    @Override
    public String getName() {
        return "mock";
    }

    @Override
    public String getPassword() {
        return "mock";
    }

    @Override
    public List<Authority> getAuthorities() {
        return new ArrayList<Authority>();
    }

    @Override
    public List<Authority> getAuthorities(Class<? extends Authority> aClass) {
        return new ArrayList<Authority>();
    }

    @Override
    public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
        return new TransferRateRequest() {
        };
    }

    @Override
    public int getMaxIdleTime() {
        return 5;
    }

    @Override
    public boolean getEnabled() {
        return true;
    }

    @Override
    public String getHomeDirectory() {
        return "";
    }
}
