package com.phicomm.remotecontrol.modules.main.screenprojection.server;

import com.phicomm.remotecontrol.modules.main.screenprojection.service.ContentDirectoryService;
import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class MediaServer {
    private UDN udn = new UDN(UUID.nameUUIDFromBytes("GNaP-MediaServer".getBytes()));
    private LocalDevice localDevice;
    private final static String deviceType = "MediaServer";
    private final static int version = 1;
    private final static String TAG = MediaServer.class.getSimpleName();
    private final static int port = 8196;
    private static InetAddress localAddress;
    private HttpServer mHttpServer;

    public MediaServer(InetAddress localAddress) throws ValidationException {
        DeviceType type = new UDADeviceType(deviceType, version);
        DeviceDetails details = new DeviceDetails(android.os.Build.MODEL,
                new ManufacturerDetails(android.os.Build.MANUFACTURER),
                new ModelDetails("GNaP", "GNaP MediaServer for Android", "v1"));
        LocalService service = new AnnotationLocalServiceBinder()
                .read(ContentDirectoryService.class);
        service.setManager(new DefaultServiceManager<ContentDirectoryService>(
                service, ContentDirectoryService.class));
        localDevice = new LocalDevice(new DeviceIdentity(udn), type, details,
                service);
        this.localAddress = localAddress;
        LogUtil.v(TAG, "MediaServer device created: ");
        LogUtil.v(TAG, "friendly name: " + details.getFriendlyName());
        LogUtil.v(TAG, "manufacturer: "
                + details.getManufacturerDetails().getManufacturer());
        LogUtil.v(TAG, "model: " + details.getModelDetails().getModelName());
        try {
            mHttpServer = new HttpServer(port);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
        LogUtil.v(TAG, "Started Http Server on port " + port);
    }

    public void restore() {
        if (null != mHttpServer) {
            mHttpServer.stop();
        }
    }

    public LocalDevice getDevice() {
        return localDevice;
    }

    public String getAddress() {
        return localAddress.getHostAddress() + ":" + port;
    }
}
