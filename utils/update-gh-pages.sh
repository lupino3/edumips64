#!/bin/bash
# Upload script taken from http://sleepycoders.blogspot.ie/2013/03/sharing-travis-ci-generated-files.html

set -e
set -u

if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  echo -n "Cloning gh-pages branch.. "
  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/lupino3/edumips64.git gh-pages > /dev/null
  echo "done."

  echo "Git branch: $TRAVIS_BRANCH"

  # Execute special per-branch actions.
  if [ "$TRAVIS_BRANCH" == "master" ]; then 
    echo -n "Building JAR.. "
    ant latest-jar > /dev/null
    echo "done."
    cp edumips64-latest.jar gh-pages
  elif [ "$TRAVIS_BRANCH" == "webui-prototype" ]; then
    echo -n "Copying web UI..."
    rm -rf gh-pages/webui
    mkdir gh-pages/webui
    cp -Rf $HOME/webui/* gh-pages/webui
    echo "done."
  fi

  JS_DIR=gh-pages/edumips64/${TRAVIS_BRANCH}
  echo "Copying the JS code to ${JS_DIR}."
  mkdir -p gh-pages/edumips64/${TRAVIS_BRANCH}
  cp war/edumips64/* gh-pages/edumips64/${TRAVIS_BRANCH}

  # Add, commit and push files.
  echo -n "Committing files.. "
  git add -f .
  git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null
  echo "done."
fi
