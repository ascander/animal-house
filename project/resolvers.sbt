val artifactoryHost = "repo.sfiqautomation.com"

val releases = "libs-release" at s"https://$artifactoryHost/artifactory/libs-release/"

def getMavenCredentials(file: File): Credentials = {
  val creds = (xml.XML.loadFile(file) \ "servers" \ "server").head
  Credentials(
    realm = "Artifactory Realm",
    host = artifactoryHost,
    userName = (creds \ "username").text,
    passwd = (creds \ "password").text)
}

resolvers += releases

credentials += getMavenCredentials(Path.userHome / ".m2" / "settings.xml")
