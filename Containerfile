FROM ubi8/ubi-minimal

COPY target/*runner /app/runner

ENTRYPOINT [ "/app/runner" ]