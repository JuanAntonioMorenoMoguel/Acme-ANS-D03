
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLog.IndicatorType;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean exist;
		TrackingLog trackingLog;
		AssistanceAgent assistanceAgent;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		exist = trackingLog != null;
		if (exist) {
			assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
			boolean isOwner = assistanceAgent.equals(trackingLog.getClaims().getAssistanceAgents());
			boolean claimPublished = !trackingLog.getClaims().isDraftMode(); // Verifica que el claim esté publicado

			super.getResponse().setAuthorised(isOwner && claimPublished);
		}
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		trackingLog.setUpdateMoment(MomentHelper.getCurrentMoment());
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolutionReason");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		if (!super.getBuffer().getErrors().hasErrors("resolutionPercentage"))
			super.state(trackingLog.getResolutionPercentage() >= 0 && trackingLog.getResolutionPercentage() <= 100, "resolutionPercentage", "trackingLog.form.error.invalidPercentage", trackingLog);

		if (!super.getBuffer().getErrors().hasErrors("indicator"))
			if ((trackingLog.getIndicator() == IndicatorType.ACCEPTED || trackingLog.getIndicator() == IndicatorType.REJECTED) && (trackingLog.getResolutionReason() == null || trackingLog.getResolutionReason().isEmpty()))
				super.state(false, "resolutionReason", "trackingLog.form.error.resolutionReasonRequired", trackingLog);

		// Validar que el Claim asociado esté publicado antes de permitir publicar el TrackingLog
		if (!super.getBuffer().getErrors().hasErrors("draftMode")) {
			boolean claimPublished = !trackingLog.getClaims().isDraftMode();
			super.state(claimPublished, "draftMode", "trackingLog.form.error.claimNotPublished", trackingLog);
		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		assert trackingLog != null;
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices indicators;
		Dataset dataset;

		indicators = SelectChoices.from(IndicatorType.class, trackingLog.getIndicator());
		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolutionReason");

		dataset.put("indicators", indicators);

		super.getResponse().addData(dataset);
	}
}
