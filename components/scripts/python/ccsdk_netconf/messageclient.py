class MessageClient:

    def __init__(self, log, mc):
        self.log = log
        self.mc = mc

    def getArtifactContent(self, artifact_name):
        content = self.mc.getArtifactContent(artifact_name)
        return content

    def getArtifactNodeMessage(self, templateName):
        content = self.mc.getArtifactNodeMessage(templateName)
        return content
