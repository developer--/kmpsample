if [ kmp ]; then
  echo "start core module downloading"
  git clone --recurse-submodules https://developer--:PAT@github.com/path-to-the-kmp-repo.git
  echo "core module download finished"
else
  echo "module already exists no need to download"
fi
