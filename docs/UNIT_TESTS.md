### Running tests

To run the test suite:

    $ python runtests.py

Don't run the flake8 code linting.

    python runtests.py --nolint

Only run the flake8 code linting, don't run the tests.

    python runtests.py --lintonly

### Test Coverage

To generate coverage report:

    $ coverage run runtests.py
    $ coverage report -m

Generate an HTML report with `coverage html` if you like.
