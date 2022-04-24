version=$(git describe --long)
jar cvfm ../jar/DecompressionPlanner-$version.jar Manifest.txt -C ../bin slaclau
