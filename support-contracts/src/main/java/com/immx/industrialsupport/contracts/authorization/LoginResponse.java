package com.immx.industrialsupport.contracts.authorization;

public record LoginResponse(String accessToken,
                            String tokenType) {
}
