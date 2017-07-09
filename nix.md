```
nix-env -i pngquant
nix-env -i protobuf
nix-env -i optipng
nix-env -i yarn

PB.runProtoc := { args => Process("protoc", args).! },

cd metadoc-js/target/scala-2.12/scalajs-bundler/main/node_modules/pngquant-bin/vendor/
rm pngquant
ln -s ~/.nix-profile/bin/pngquant pngquant

cd metadoc-js/target/scala-2.12/scalajs-bundler/main/node_modules/optipng-bin/vendor
rm optipng
ln -s ~/.nix-profile/bin/optipng optipng
```