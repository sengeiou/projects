import itchat


@itchat.msg_register(itchat.content.TEXT)
def text_link(msg):
    print('msg: %s' % msg)
    return msg.text


@itchat.msg_register([PICTURE, ATTACHMENT, VIDEO])
def download_files(msg):
    print('msg: %s' % msg)
    
    """ msg.download(msg.fileName)
    typeSymbol = {
        PICTURE: 'img',
        VIDEO: 'vid', }.get(msg.type, 'fil') 
    return '@%s@%s' % (typeSymbol, msg.fileName)"""


itchat.auto_login()
itchat.run()
