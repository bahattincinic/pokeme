FROM python:3.6
RUN apt-get -y update && apt-get install -y --no-install-recommends apt-utils
RUN apt-get -y update && apt-get install -y --no-install-recommends && \
    apt-get install -y python3-dev && \
    apt-get install -y build-essential && \
    apt-get install -y libpq-dev && \
    apt-get install -y libreadline-dev
RUN pip install --upgrade pip
RUN pip install --upgrade setuptools
COPY ./requirements/base.txt base.txt
COPY ./requirements/dev.txt requirements.txt
RUN pip install -r requirements.txt
RUN rm requirements.txt
RUN rm base.txt

WORKDIR /api
CMD [ "python" ]