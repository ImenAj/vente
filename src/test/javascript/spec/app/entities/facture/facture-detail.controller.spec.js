'use strict';

describe('Controller Tests', function() {

    describe('Facture Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFacture, MockClient, MockBons_de_livraison;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFacture = jasmine.createSpy('MockFacture');
            MockClient = jasmine.createSpy('MockClient');
            MockBons_de_livraison = jasmine.createSpy('MockBons_de_livraison');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Facture': MockFacture,
                'Client': MockClient,
                'Bons_de_livraison': MockBons_de_livraison
            };
            createController = function() {
                $injector.get('$controller')("FactureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:factureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
