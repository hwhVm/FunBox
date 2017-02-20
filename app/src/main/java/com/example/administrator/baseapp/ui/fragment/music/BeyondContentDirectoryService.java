package com.example.administrator.baseapp.ui.fragment.music;

import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.io.File;
import java.util.List;

/**
 * Created by beini on 2017/2/20.
 */

public class BeyondContentDirectoryService extends AbstractContentDirectoryService {


    public static final DIDLObject.Class AUDIO_CLASS = new DIDLObject.Class("object.container.audio");
    public static final DIDLObject.Class VIDEO_CLASS = new DIDLObject.Class("object.container.video");

    public final static String ROOT = "0";
    public final static String AUDIO = ROOT + File.separator + "10";
    public final static String VIDEO = ROOT + File.separator + "20";
    public final static String IMAGE = ROOT + File.separator + "30";

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag, String filter, long firstResult, long maxResults, SortCriterion[] orderby) throws ContentDirectoryException {
        String address = Utils.getIPAddress(true);
        String serverUrl = "http://" + address + ":" + JettyResourceServer.JETTY_SERVER_PORT;

        //Create container by id
        Container resultBean = ContainerFactory.createContainer(objectID, serverUrl);
        DIDLContent content = new DIDLContent();

        for (Container c : resultBean.getContainers())
            content.addContainer(c);

        for (Item item : resultBean.getItems())
            content.addItem(item);

        int count = resultBean.getChildCount();
        String contentModel = "";
        try {
            contentModel = new DIDLParser().generate(content);
        } catch (Exception e) {
            throw new ContentDirectoryException(
                    ContentDirectoryErrorCode.CANNOT_PROCESS, e.toString());
        }

        return new BrowseResult(contentModel, count, count);
    }

    static class ContainerFactory {

        static Container createContainer(String containerId, String serverUrl) {
            Container result = new Container();
            result.setChildCount(0);

            if (ROOT.equals(containerId)) {
//                Container audioContainer = new Container();
//                audioContainer.setId(AUDIO);
//                audioContainer.setParentID(ROOT);
//                audioContainer.setClazz(AUDIO_CLASS);
//                audioContainer.setTitle("Audios");
//
//                result.addContainer(audioContainer);
//                result.setChildCount(result.getChildCount() + 1);
//
//                Container videoContainer = new Container();
//                videoContainer.setId(VIDEO);
//                videoContainer.setParentID(ROOT);
//                videoContainer.setClazz(VIDEO_CLASS);
//                videoContainer.setTitle("Videos");
//
//                result.addContainer(videoContainer);
//                result.setChildCount(result.getChildCount() + 1);
                //Get audio items
                List<Item> items = MediaResourceDao.getAudioList(serverUrl, ROOT);

                for (Item item : items) {
                    result.addItem(item);
                    result.setChildCount(result.getChildCount() + 1);
                }
            } else if (AUDIO.equals(containerId)) {
                //Get audio items
                List<Item> items = MediaResourceDao.getAudioList(serverUrl, AUDIO);

                for (Item item : items) {
                    result.addItem(item);
                    result.setChildCount(result.getChildCount() + 1);
                }
            } else if (VIDEO.equals(containerId)) {
                //Get video items
                List<Item> items = MediaResourceDao.getVideoList(serverUrl, VIDEO);

                for (Item item : items) {
                    result.addItem(item);
                    result.setChildCount(result.getChildCount() + 1);
                }
            }
            return result;
        }


    }
}
