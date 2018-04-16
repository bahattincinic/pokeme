# API Installation with Docker

Build:

```bash
$ docker-compose up -d --build
```

Create Database Tables:

```bash
$ docker-compose exec api /bin/bash -c 'apistar create_tables'
```

Start project:

```bash
$ docker-compose start
```
