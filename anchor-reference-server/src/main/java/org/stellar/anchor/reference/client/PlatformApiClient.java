package org.stellar.anchor.reference.client;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.stellar.anchor.exception.AnchorException;
import org.stellar.anchor.util.OkHttpUtil;
import org.stellar.platform.apis.platform.requests.PatchTransactionsRequest;
import org.stellar.platform.apis.platform.responses.GetTransactionResponse;
import org.stellar.platform.apis.platform.responses.PatchTransactionsResponse;

public class PlatformApiClient extends BaseApiClient {
  private String endpoint;

  public PlatformApiClient(String endpoint) {
    this.endpoint = endpoint;
  }

  public GetTransactionResponse getTransaction(String id) throws IOException, AnchorException {
    Request request =
        new Request.Builder()
            .url(endpoint + "/transactions/$id")
            .header("Content-Type", "application/json")
            .get()
            .build();
    String responseBody = handleResponse(client.newCall(request).execute());
    return gson.fromJson(responseBody, GetTransactionResponse.class);
  }

  public PatchTransactionsResponse patchTransaction(PatchTransactionsRequest txnRequest)
      throws IOException, AnchorException {
    HttpUrl url = HttpUrl.parse(endpoint);
    url =
        new HttpUrl.Builder()
            .scheme(url.scheme())
            .host(url.host())
            .port(url.port())
            .addPathSegment("transactions")
            .build();

    RequestBody requestBody = OkHttpUtil.buildJsonRequestBody(gson.toJson(txnRequest));
    Request request =
        new Request.Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build();
    var response = client.newCall(request).execute();
    return gson.fromJson(handleResponse(response), PatchTransactionsResponse.class);
  }
}