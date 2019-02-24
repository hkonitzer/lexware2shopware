package space.schellenberger.etl.shopware2lexware.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

public class ShopwareResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
            String msg = String.format("CategoryAPIService konnte API unter %s mit %s nicht abrufen - Status: %s Text: %s", url, method, response.getStatusCode(), response.getStatusText());
            //log.error(msg);
            throw new IOException(msg);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            handleError(null, null, response);
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode().isError()) {
                return (response.getStatusCode() != HttpStatus.NOT_FOUND);
            }
            return false;
        }


    }
