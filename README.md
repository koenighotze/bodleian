# Bodleian - a library service for media

[![Build main or features](https://github.com/koenighotze/bodleian/actions/workflows/build.yml/badge.svg)](https://github.com/koenighotze/bodleian/actions/workflows/build.yml)
[![Deploy tagged version](https://github.com/koenighotze/bodleian/actions/workflows/deploy-tagged-version.yml/badge.svg)](https://github.com/koenighotze/bodleian/actions/workflows/deploy-tagged-version.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/4650d0135ac14d25bada60540324e39d)](https://www.codacy.com/gh/koenighotze/bodleian/dashboard?utm_source=github.com&utm_medium=referral&utm_content=koenighotze/bodleian&utm_campaign=Badge_Coverage)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4650d0135ac14d25bada60540324e39d)](https://www.codacy.com/gh/koenighotze/bodleian/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=koenighotze/bodleian&amp;utm_campaign=Badge_Grade)
![Known Vulnerabilities](https://snyk.io/test/github/koenighotze/bodleian/badge.svg)

This is the Bodleian backend service, deployed to Google Cloud Run.

* See infrastructure [here](https://github.com/koenighotze/bodleian-infrastructure).
* Frontend is [here](https://github.com/koenighotze/bodleian-frontend)

## Getting started

```bash

$ ./mvnw spring-boot:run
...
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.3)

$ http localhost:8080/actuator/health
HTTP/1.1 200 OK
Content-Length: 15
Content-Type: application/vnd.spring-boot.actuator.v3+json

{
    "status": "UP"
}
```

## Calling the service

Fetch the URL of the deployed service

```bash
$ URL=$(gcloud run services describe bodleian --format json | jq  -r '.status.url')
```

Login and rememeber the token

```bash
$ AUTH=$(gcloud auth print-identity-token)
```

Call the service

```bash
$ curl -H "Authorization: Bearer $AUTH" $URL/actuator/health

{"status":"UP"}
```