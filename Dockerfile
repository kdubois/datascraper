FROM ubi8/ubi-minimal

COPY /target/quarkus-app /app

ENTRYPOINT [ "/app" ]